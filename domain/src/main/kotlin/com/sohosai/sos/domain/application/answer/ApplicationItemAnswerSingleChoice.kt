package com.sohosai.sos.domain.application.answer

data class ApplicationItemAnswerSingleChoice(
    override val itemId: Int,
    val selectedOptionId: Int
) : ApplicationItemAnswer