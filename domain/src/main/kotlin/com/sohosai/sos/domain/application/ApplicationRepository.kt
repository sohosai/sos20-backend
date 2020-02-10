package com.sohosai.sos.domain.application

import com.sohosai.sos.domain.application.answer.ApplicationItemAnswer
import com.sohosai.sos.domain.application.condition.ApplicationConditions
import com.sohosai.sos.domain.application.item.ApplicationItem
import java.util.*

interface ApplicationRepository {
    suspend fun createApplication(
        name: String,
        description: String,
        authorId: UUID,
        items: List<ApplicationItem>,
        conditions: ApplicationConditions
    ): Application

    suspend fun createApplicationAnswer(
        applicationId: Int,
        projectId: Int,
        answers: List<ApplicationItemAnswer>
    )
}