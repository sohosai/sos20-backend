package com.sohosai.sos.domain.application.item

data class ApplicationItemCondition(
    val itemId: Int,
    val optionId: Int,
    val shouldBeSelected: Boolean
)