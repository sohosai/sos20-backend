package com.sohosai.sos.domain.project

import java.util.*

data class Project(
    val id: UUID,
    val ownerId: UUID,
    val subOwnerId: UUID?,
    val name: String,
    val kanaName: String,
    val groupName: String,
    val kanaGroupName: String,
    val description: String,
    val category: ProjectCategory,
    val attributes: List<ProjectAttribute>
)