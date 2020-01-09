package com.sohosai.sos.service

import com.sohosai.sos.domain.project.Project
import com.sohosai.sos.domain.project.ProjectAttribute
import com.sohosai.sos.domain.project.ProjectCategory
import com.sohosai.sos.domain.project.ProjectRepository
import com.sohosai.sos.domain.user.UserRepository
import java.util.*

class ProjectService(
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository
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
            throw IllegalArgumentException("The user is already the owner of a project. UserId: ${ownerId}, project: ${it.id}")
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
}