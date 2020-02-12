package com.sohosai.sos.domain.application.condition.conditions

import com.sohosai.sos.domain.application.condition.ApplicationCondition
import com.sohosai.sos.domain.project.Project

data class ApplicationConditionAnswered(
    val applicationId: Int,
    val shouldBeSelected: Boolean
) : ApplicationCondition {
    override fun isMatchForProject(project: Project) {

    }
}