package com.sohosai.sos.domain.application.condition

data class ApplicationConditions(
    val op: ApplicationConditionsOp,
    val conditions: List<ApplicationCondition>
)