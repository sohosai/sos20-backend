package com.sohosai.sos.interfaces

import com.sohosai.sos.domain.KOIN
import com.sohosai.sos.domain.user.User
import com.sohosai.sos.domain.user.UserRepository

// TODO: inject this by right way
private val userRepository: UserRepository by KOIN.inject()

suspend fun AuthContext.toUser(): User {
    return userRepository.findUserByAuthId(authId) ?: throw HttpStatusCodeException(401, "Not registered yet")
}