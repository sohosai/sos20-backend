package com.sohosai.sos.interfaces

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.sohosai.sos.domain.user.User
import com.sohosai.sos.service.UserService
import graphql.schema.DataFetchingEnvironment

@Suppress("unused")
class UserQueryResolver(private val userService: UserService) : GraphQLQueryResolver {
    suspend fun listUsers(
        environment: DataFetchingEnvironment
    ): List<User> {
        val context = environment.getContext<AuthContext>()
        val user = requireNotNull(context.toUser())

        return userService.listUsers(user)
    }
}