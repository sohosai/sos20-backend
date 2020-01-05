package com.sohosai.sos.interfaces.project

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.sohosai.sos.domain.project.Project
import com.sohosai.sos.domain.project.ProjectAttribute
import com.sohosai.sos.domain.project.ProjectCategory
import com.sohosai.sos.interfaces.AuthContext
import com.sohosai.sos.interfaces.toUser
import com.sohosai.sos.service.project.ProjectService
import graphql.schema.DataFetchingEnvironment

@Suppress("unused")
class ProjectMutationResolver(
    private val projectService: ProjectService
) : GraphQLMutationResolver {
    suspend fun createProject(
        name: String,
        kanaName: String,
        groupName: String,
        kanaGroupName: String,
        description: String,
        category: ProjectCategory,
        attributes: List<ProjectAttribute>,
        environment: DataFetchingEnvironment
    ): Project {
        val context = environment.getContext<AuthContext>()
        return projectService.createProject(
            ownerId = context.toUser().id,
            subOwnerId = null,
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