package com.sohosai.sos.service

import com.sohosai.sos.domain.user.*
import com.sohosai.sos.service.exception.UserNotFoundException
import java.util.*

class UserService(private val userRepository: UserRepository) {
    suspend fun createUser(
        name: String,
        kanaName: String,
        email: Email,
        phoneNumber: PhoneNumber,
        studentId: String,
        affiliationName: String,
        affiliationType: AffiliationType,
        role: Role,
        authId: String
        ): User {
        return userRepository.createUser(
            name = name,
            kanaName = kanaName,
            email = email,
            phoneNumber = phoneNumber,
            studentId = studentId,
            affiliationName = affiliationName,
            affiliationType = affiliationType,
            role = role,
            authId = authId
        )
    }

    suspend fun getUserById(userId: UUID): User {
        return userRepository.findUserById(userId) ?: throw UserNotFoundException(userId)
    }

    suspend fun listUsers(viewer: User): List<User> {
        if (!viewer.hasPrivilege(Role.LEADER)) {
            // TODO: improve error handling
            throw IllegalStateException("Not enough permission")
        }

        return userRepository.listUsers()
    }
}