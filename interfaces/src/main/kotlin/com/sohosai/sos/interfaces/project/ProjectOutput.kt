package com.sohosai.sos.interfaces.project

import com.sohosai.sos.domain.project.Project
import com.sohosai.sos.domain.project.ProjectAttribute
import com.sohosai.sos.domain.project.ProjectCategory
import java.util.*

data class ProjectOutput(
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
    companion object {
        fun fromProject(project: Project): ProjectOutput {
            return ProjectOutput(
                ownerId = project.ownerId,
                subOwnerId = project.subOwnerId,
                name = project.name,
                kanaName = project.kanaName,
                groupName = project.groupName,
                kanaGroupName = project.kanaGroupName,
                description = project.description,
                category = project.category,
                attributes = project.attributes
            )
        }
    }
}