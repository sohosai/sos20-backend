package com.sohosai.sos.domain.application.json

import com.sohosai.sos.domain.application.Application
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*

private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

data class ApplicationJson(
    val id: Int,
    val name: String,
    val description: String,
    val authorId: UUID,
    val items: List<ApplicationItemJson>,
    val conditions: ApplicationConditionsJson,
    val startDate: String,
    val endDate: String
) {
    companion object {
        fun fromApplication(application: Application): ApplicationJson {
            return ApplicationJson(
                id = application.id,
                name = application.name,
                description = application.description,
                authorId = application.authorId,
                items = application.items.map { ApplicationItemJson.fromApplicationItem(it) },
                conditions = ApplicationConditionsJson.fromApplicationConditions(application.conditions),
                startDate = application.startDate.format(dateFormatter),
                endDate = application.endDate.format(dateFormatter)
            )
        }
    }
}