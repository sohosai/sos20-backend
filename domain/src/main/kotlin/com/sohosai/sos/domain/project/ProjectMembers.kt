package com.sohosai.sos.domain.project

import com.sohosai.sos.domain.user.User

data class ProjectMembers(
    val owner: User,
    val subOwner: User?
)