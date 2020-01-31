package com.sohosai.sos.interfaces.user

import com.sohosai.sos.domain.user.AffiliationType

data class CreateUserOutput(
    val name: String,
    val kanaName: String,
    val email: String,
    val phoneNumber: String,
    val studentId: String,
    val affiliationName: String,
    val affiliationType: AffiliationType
)