package com.sohosai.sos.service

import com.sohosai.sos.domain.user.*

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
}