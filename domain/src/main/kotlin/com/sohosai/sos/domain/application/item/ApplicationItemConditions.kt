package com.sohosai.sos.domain.application.item

import com.sohosai.sos.domain.application.condition.ApplicationConditionsOp

data class ApplicationItemConditions(
    val op: ApplicationConditionsOp,
    val conditions: List<ApplicationItemCondition>
)