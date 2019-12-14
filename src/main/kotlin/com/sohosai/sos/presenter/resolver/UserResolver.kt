package com.sohosai.sos.presenter.resolver

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.sohosai.sos.domain.auth.AuthContext
import com.sohosai.sos.domain.user.*
import com.sohosai.sos.service.UserService
import graphql.schema.DataFetchingEnvironment

@Suppress("unused")
class UserResolver(private val userService: UserService) : GraphQLQueryResolver {

    suspend fun createUser(
        name: String,
        kanaName: String,
        phoneNumber: String,
        studentId: String,
        affiliationName: String,
        affiliationType: AffiliationType,
        environment: DataFetchingEnvironment
    ): User {
        val context = environment.getContext<AuthContext>()

        // TODO: validate
        return userService.createUser(
            name = name,
            kanaName = kanaName,
            email = context.email,
            phoneNumber = PhoneNumber(phoneNumber),
            studentId = studentId,
            affiliationName = affiliationName,
            affiliationType = affiliationType,
            role = Role.GENERAL,
            authId = context.authId
        )
    }
}