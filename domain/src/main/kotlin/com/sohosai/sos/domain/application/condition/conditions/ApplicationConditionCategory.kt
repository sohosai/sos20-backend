package com.sohosai.sos.domain.application.condition.conditions

import com.sohosai.sos.domain.application.condition.ApplicationCondition
import com.sohosai.sos.domain.project.ProjectCategory

data class ApplicationConditionCategory(
    val category: ProjectCategory
) : ApplicationCondition