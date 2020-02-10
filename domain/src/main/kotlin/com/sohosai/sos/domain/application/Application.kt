package com.sohosai.sos.domain.application

import com.sohosai.sos.domain.application.condition.ApplicationConditions
import com.sohosai.sos.domain.application.item.ApplicationItem
import java.util.*

data class Application(
    val id: Int,
    val name: String,
    val description: String,
    val authorId: UUID,
    val items: List<ApplicationItem>,
    val conditions: ApplicationConditions
)