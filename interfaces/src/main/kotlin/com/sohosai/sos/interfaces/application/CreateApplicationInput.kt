package com.sohosai.sos.interfaces.application

import com.sohosai.sos.domain.application.json.ApplicationConditionsJson
import com.sohosai.sos.domain.application.json.ApplicationItemJson

data class CreateApplicationInput(
    val name: String,
    val description: String,
    val items: List<ApplicationItemJson>,
    val conditions: ApplicationConditionsJson?,
    val startDate: String,
    val endDate: String
)