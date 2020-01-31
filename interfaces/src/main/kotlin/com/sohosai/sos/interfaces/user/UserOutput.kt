package com.sohosai.sos.interfaces.user

import com.sohosai.sos.domain.user.AffiliationType
import com.sohosai.sos.domain.user.User

data class UserOutput(
    val name: String,
    val kanaName: String,
    val email: String,
    val phoneNumber: String,
    val studentId: String,
    val affiliationName: String,
    val affiliationType: AffiliationType
) {
    companion object {
        fun fromUser(user: User): UserOutput {
            return UserOutput(
                name = user.name,
                kanaName = user.kanaName,
                email = user.email.value,
                phoneNumber = user.phoneNumber.value,
                studentId = user.studentId,
                affiliationName = user.affiliation.name,
                affiliationType = user.affiliation.type
            )
        }
    }
}