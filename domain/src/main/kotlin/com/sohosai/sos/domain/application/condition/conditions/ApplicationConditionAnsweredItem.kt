package com.sohosai.sos.domain.application.condition.conditions

import com.sohosai.sos.domain.application.condition.ApplicationCondition
import com.sohosai.sos.domain.application.item.ApplicationItemCondition

data class ApplicationConditionAnsweredItem(
    val applicationId: Int,
    val itemCondition: ApplicationItemCondition
) : ApplicationCondition