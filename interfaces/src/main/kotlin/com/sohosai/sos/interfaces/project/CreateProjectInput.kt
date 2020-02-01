package com.sohosai.sos.interfaces.project

import com.sohosai.sos.domain.project.ProjectAttribute
import com.sohosai.sos.domain.project.ProjectCategory

data class CreateProjectInput(
    val name: String,
    val kanaName: String,
    val groupName: String,
    val kanaGroupName: String,
    val description: String,
    val category: ProjectCategory,
    val attributes: List<ProjectAttribute>
)