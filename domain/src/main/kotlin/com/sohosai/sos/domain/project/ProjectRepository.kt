package com.sohosai.sos.domain.project

import java.util.*

interface ProjectRepository {
    suspend fun createProject(
        ownerId: UUID,
        subOwnerId: UUID?,
        name: String,
        kanaName: String,
        groupName: String,
        kanaGroupName: String,
        description: String,
        category: ProjectCategory,
        attributes: List<ProjectAttribute>
    ): Project

    suspend fun findById(id: Int): Project?

    suspend fun findProjectByOwner(ownerId: UUID): Project?

    suspend fun listProjects(): List<Project>
}