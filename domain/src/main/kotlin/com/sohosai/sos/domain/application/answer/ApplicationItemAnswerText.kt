package com.sohosai.sos.domain.application.answer

data class ApplicationItemAnswerText(
    override val itemId: Int,
    val content: String
) : ApplicationItemAnswer