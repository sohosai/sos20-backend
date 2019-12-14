package com.sohosai.sos.domain.auth

import com.sohosai.sos.domain.user.Email
import io.ktor.auth.Principal

data class AuthContext(
    val email: Email,
    val isEmailVerified: Boolean
): Principal