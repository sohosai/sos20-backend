package com.sohosai.sos.domain.application.answer

data class ApplicationItemAnswerMultipleChoice(
    override val itemId: Int,
    val selectedOptionId: Int
) : ApplicationItemAnswer