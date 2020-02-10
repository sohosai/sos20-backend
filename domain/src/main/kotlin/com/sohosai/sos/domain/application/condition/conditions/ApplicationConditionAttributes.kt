package com.sohosai.sos.domain.application.condition.conditions

import com.sohosai.sos.domain.application.condition.ApplicationCondition
import com.sohosai.sos.domain.project.ProjectAttribute

class ApplicationConditionAttributes(
    val attributes: List<ProjectAttribute>
) : ApplicationCondition