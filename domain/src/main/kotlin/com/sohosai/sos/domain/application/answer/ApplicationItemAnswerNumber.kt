package com.sohosai.sos.domain.application.answer

data class ApplicationItemAnswerNumber(
    override val itemId: Int,
    val value: Int
) : ApplicationItemAnswer