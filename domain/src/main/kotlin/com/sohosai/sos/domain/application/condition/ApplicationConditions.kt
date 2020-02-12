package com.sohosai.sos.domain.application.condition

import com.sohosai.sos.domain.project.Project

data class ApplicationConditions(
    val op: ApplicationConditionsOp,
    val conditions: List<ApplicationCondition>
) {
    fun isMatchForProject(project: Project) {

    }
}