package com.sohosai.sos.interfaces

import com.sohosai.sos.domain.user.Email

data class AuthContext(
    val authId: String,
    val email: Email,
    val isEmailVerified: Boolean
)