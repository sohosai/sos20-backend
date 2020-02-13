package com.sohosai.sos.domain.project

import com.sohosai.sos.domain.user.Role
import com.sohosai.sos.domain.user.User
import java.util.*

data class Project(
    val id: Int,
    val ownerId: UUID,
    val subOwnerId: UUID?,
    val name: String,
    val kanaName: String,
    val groupName: String,
    val kanaGroupName: String,
    val description: String,
    val category: ProjectCategory,
    val attributes: List<ProjectAttribute>
) {
    fun canAccessBy(user: User): Boolean {
        return this.ownerId == user.id || this.subOwnerId == user.id || user.hasPrivilege(Role.COMMITTEE)
    }
}