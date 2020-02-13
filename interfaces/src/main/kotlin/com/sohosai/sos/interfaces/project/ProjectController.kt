package com.sohosai.sos.interfaces.project

import com.sohosai.sos.domain.application.json.ApplicationJson
import com.sohosai.sos.interfaces.AuthContext
import com.sohosai.sos.interfaces.HttpStatusCodeException
import com.sohosai.sos.interfaces.toUser
import com.sohosai.sos.service.ProjectService

class ProjectController(private val projectService: ProjectService) {
    suspend fun createProject(input: CreateProjectInput, context: AuthContext): ProjectOutput {
        val owner = context.toUser()
        val project = projectService.createProject(
            ownerId = owner.id,
            subOwnerId = null,
            name = input.name,
            kanaName = input.kanaName,
            groupName = input.groupName,
            kanaGroupName = input.kanaGroupName,
            description = input.description,
            category = input.category,
            attributes = input.attributes
        )

        return ProjectOutput.fromProject(project)
    }

    suspend fun getProject(rawId: String, context: AuthContext): ProjectOutput {
        val project = projectService.getProject(
            id = rawId.toInt(),
            caller = context.toUser()
        ) ?: throw HttpStatusCodeException(404, "Project not found.")

        return ProjectOutput.fromProject(project)
    }

    suspend fun listProjects(context: AuthContext): List<ProjectOutput> {
        val projects = projectService.listProjects(context.toUser())

        return projects.map { ProjectOutput.fromProject(it) }
    }

    suspend fun getProjectMembers(rawId: String, context: AuthContext): ProjectMembersOutput {
        val members = projectService.getProjectMembers(
            projectId = rawId.toInt(),
            caller = context.toUser()
        )

        return ProjectMembersOutput.fromProjectMembers(members)
    }

    suspend fun getNotAnsweredApplications(rawProjectId: String, context: AuthContext): List<ApplicationJson> {
        val applications = projectService.getNotAnsweredApplications(
            projectId = rawProjectId.toInt(),
            caller = context.toUser()
        )

        return applications.map { ApplicationJson.fromApplication(it) }
    }
}