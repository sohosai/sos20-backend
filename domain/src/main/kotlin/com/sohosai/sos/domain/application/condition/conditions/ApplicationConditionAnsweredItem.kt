package com.sohosai.sos.domain.application.condition.conditions

import com.sohosai.sos.domain.application.condition.ApplicationCondition
import com.sohosai.sos.domain.application.item.ApplicationItemConditions

data class ApplicationConditionAnsweredItem(
    val applicationId: Int,
    val itemCondition: ApplicationItemConditions
) : ApplicationCondition