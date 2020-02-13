package com.sohosai.sos.service

import com.sohosai.sos.domain.application.Application
import com.sohosai.sos.domain.application.ApplicationRepository
import com.sohosai.sos.domain.project.*
import com.sohosai.sos.domain.user.Role
import com.sohosai.sos.domain.user.User
import com.sohosai.sos.domain.user.UserRepository
import com.sohosai.sos.service.exception.NotEnoughPermissionException
import java.util.*

class ProjectService(
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository,
    private val applicationRepository: ApplicationRepository
) {
    suspend fun createProject(
        ownerId: UUID,
        subOwnerId: UUID?,
        name: String,
        kanaName: String,
        groupName: String,
        kanaGroupName: String,
        description: String,
        category: ProjectCategory,
        attributes: List<ProjectAttribute>
    ): Project {
        val owner = requireNotNull(userRepository.findUserById(ownerId)) { "The user not found for that id: $ownerId" }
        val subOwner = subOwnerId?.let {
            requireNotNull(userRepository.findUserById(subOwnerId)) { "The user not found for that id: $subOwnerId" }
        }
        projectRepository.findProjectByOwner(ownerId)?.let {
            throw IllegalArgumentException("The user already own a project. UserId: ${ownerId}, project: ${it.id}")
        }

        return projectRepository.createProject(
            ownerId = owner.id,
            subOwnerId = subOwner?.id,
            name = name,
            kanaName = kanaName,
            groupName = groupName,
            kanaGroupName = kanaGroupName,
            description = description,
            category = category,
            attributes = attributes
        )
    }

    suspend fun getProject(id: Int, caller: User): Project? {
        val project = projectRepository.findById(id) ?: return null

        return if (project.canAccessBy(caller)) {
            project
        } else {
            null
        }
    }

    suspend fun listProjects(caller: User): List<Project> {
        if (!caller.hasPrivilege(Role.COMMITTEE)) {
            throw NotEnoughPermissionException()
        }

        return projectRepository.listProjects()
    }

    suspend fun getProjectMembers(projectId: Int, caller: User): ProjectMembers {
        val project = projectRepository.findById(projectId) ?: throw IllegalArgumentException("Project not found. projectId: $projectId")
        if (!project.canAccessBy(caller)) {
            throw IllegalArgumentException("Project not found. projectId: $projectId")
        }

        val ids = mutableListOf(project.ownerId).apply {
            if (project.subOwnerId != null) {
                add(project.subOwnerId!!)
            }
        }
        val members = userRepository.findUsersById(ids)

        return ProjectMembers(
            owner = members[0],
            subOwner = members.getOrNull(1)
        )
    }

    suspend fun getNotAnsweredApplications(projectId: Int, caller: User): List<Application> {
        val project = projectRepository.findById(projectId) ?: throw IllegalArgumentException("Project not found. projectId: $projectId")
        if (!project.canAccessBy(caller)) {
            throw IllegalArgumentException("Project not found. projectId: $projectId")
        }

        return applicationRepository.listNotAnsweredApplicationByProjectId(projectId)
    }
}