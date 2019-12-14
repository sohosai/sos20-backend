package com.sohosai.sos.domain.auth

import com.sohosai.sos.domain.user.Email
import com.sohosai.sos.domain.user.User
import com.sohosai.sos.domain.user.UserRepository
import com.sohosai.sos.infrastructure.application
import io.ktor.auth.Principal
import org.koin.ktor.ext.inject

// TODO: inject this by right way
private val userRepository: UserRepository by application.inject()

data class AuthContext(
    val authId: String,
    val email: Email,
    val isEmailVerified: Boolean
): Principal {

    suspend fun toUser(): User? {
        return userRepository.findUserByAuthId(authId)
    }
}