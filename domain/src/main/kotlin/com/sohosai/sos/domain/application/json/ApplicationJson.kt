package com.sohosai.sos.domain.application.json

import com.sohosai.sos.domain.application.Application
import java.util.*

data class ApplicationJson(
    val id: Int,
    val name: String,
    val description: String,
    val authorId: UUID,
    val items: List<ApplicationItemJson>,
    val conditions: ApplicationConditionsJson
) {
    companion object {
        fun fromApplication(application: Application): ApplicationJson {
            return ApplicationJson(
                id = application.id,
                name = application.name,
                description = application.description,
                authorId = application.authorId,
                items = application.items.map { ApplicationItemJson.fromApplicationItem(it) },
                conditions = ApplicationConditionsJson.fromApplicationConditions(application.conditions)
            )
        }
    }
}