package com.sohosai.sos.domain.user

import com.sohosai.sos.domain.KOIN
import com.sohosai.sos.domain.project.Project
import com.sohosai.sos.domain.project.ProjectRepository
import java.util.*

private val projectRepository: ProjectRepository by KOIN.inject()

data class User(
    val id: UUID,
    val name: String,
    val kanaName: String,
    val email: Email,
    val phoneNumber: PhoneNumber,
    val studentId: String,
    val affiliation: Affiliation,
    val role: Role
) {
    fun hasPrivilege(role: Role): Boolean {
        return role.ordinal >= this.role.ordinal
    }

    suspend fun owningProject(): Project? {
        return projectRepository.findProjectByOwner(id)
    }

    suspend fun subOwningProject(): Project? {
        return projectRepository.findProjectBySubOwner(id)
    }

}