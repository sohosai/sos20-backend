package com.sohosai.sos.domain.user

import java.util.*

interface UserRepository {
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
    ): User

    suspend fun listUsers(): List<User>

    suspend fun findUserById(id: UUID): User?

    suspend fun findUserByAuthId(authId: String): User?
}