package com.sohosai.sos.interfaces.application

import com.sohosai.sos.domain.application.json.ApplicationItemAnswerJson

data class AnswerApplicationInput(
    val projectId: Int,
    val answers: List<ApplicationItemAnswerJson>
)