package com.sohosai.sos.interfaces.project

import com.coxautodev.graphql.tools.GraphQLResolver
import com.sohosai.sos.domain.project.Project
import com.sohosai.sos.domain.user.User
import com.sohosai.sos.domain.user.UserRepository

@Suppress("unused")
class ProjectResolver(
    private val userRepository: UserRepository
) : GraphQLResolver<Project> {
    suspend fun getOwner(project: Project): User {
        return requireNotNull(userRepository.findUserById(project.ownerId))
    }

    suspend fun getSubOwner(project: Project): User? {
        return project.subOwnerId?.let {
            userRepository.findUserById(it)
        }
    }
}