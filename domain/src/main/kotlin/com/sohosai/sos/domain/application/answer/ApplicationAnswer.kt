package com.sohosai.sos.domain.application.answer

data class ApplicationAnswer(
    val applicationId: Int,
    val projectId: Int,
    val answers: List<ApplicationItemAnswer>
)