package com.sohosai.sos.domain.project

import com.sohosai.sos.domain.project.Project
import com.sohosai.sos.domain.project.ProjectAttribute
import com.sohosai.sos.domain.project.ProjectCategory
import com.sohosai.sos.domain.user.User
import java.util.*

interface ProjectRepository {
    suspend fun createProject(
        owner: User,
        subOwner: User?,
        name: String,
        kanaName: String,
        groupName: String,
        kanaGroupName: String,
        description: String,
        category: ProjectCategory,
        attributes: List<ProjectAttribute>
    ): Project

    suspend fun findProjectByOwner(ownerId: UUID): Project?
}