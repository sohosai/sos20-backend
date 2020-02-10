package com.sohosai.sos.domain.application.condition.conditions

import com.sohosai.sos.domain.application.condition.ApplicationCondition

data class ApplicationConditionAnswered(
    val applicationId: Int
) : ApplicationCondition