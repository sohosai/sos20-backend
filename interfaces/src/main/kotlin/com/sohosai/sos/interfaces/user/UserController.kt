package com.sohosai.sos.interfaces.user

import com.sohosai.sos.domain.user.PhoneNumber
import com.sohosai.sos.domain.user.Role
import com.sohosai.sos.interfaces.AuthContext
import com.sohosai.sos.interfaces.toUser
import com.sohosai.sos.service.UserService
import java.util.*

class UserController(private val userService: UserService) {
    suspend fun createUser(input: CreateUserInput, context: AuthContext): UserOutput {
        val user = userService.createUser(
            name = input.name,
            kanaName = input.kanaName,
            email = context.email,
            phoneNumber = PhoneNumber(input.phoneNumber),
            studentId = input.studentId,
            affiliationName = input.affiliationName,
            affiliationType = input.affiliationType,
            role = Role.GENERAL,
            authId = context.authId
        )

        return UserOutput.fromUser(user)
    }

    suspend fun getUser(rawUserId: String, context: AuthContext): UserOutput {
        val user = userService.getUserById(
            userId = UUID.fromString(rawUserId),
            caller = context.toUser()
        )

        return UserOutput.fromUser(user)
    }

    suspend fun loginUser(context: AuthContext): UserOutput {
        return UserOutput.fromUser(context.toUser())
    }

    suspend fun listUsers(context: AuthContext): List<UserOutput> {
        val users = userService.listUsers(context.toUser())

        return users.map { UserOutput.fromUser(it) }
    }
}