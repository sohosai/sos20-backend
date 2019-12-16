package com.sohosai.sos.domain.auth

import io.ktor.auth.AuthenticationFailedCause
import io.ktor.auth.Principal

sealed class AuthStatus : Principal {
    data class Authorized(
        val context: AuthContext
    ) : AuthStatus()
    data class Unauthorized(
        val cause: AuthenticationFailedCause
    ) : AuthStatus()
}