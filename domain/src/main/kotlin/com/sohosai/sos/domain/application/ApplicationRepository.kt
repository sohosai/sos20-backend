package com.sohosai.sos.domain.application

import com.sohosai.sos.domain.application.answer.ApplicationItemAnswer
import com.sohosai.sos.domain.application.answer.ProjectsApplicationAnswer
import com.sohosai.sos.domain.application.condition.ApplicationConditions
import com.sohosai.sos.domain.application.item.ApplicationItem
import java.time.LocalDate
import java.util.*

interface ApplicationRepository {
    suspend fun createApplication(
        name: String,
        description: String,
        authorId: UUID,
        items: List<ApplicationItem>,
        conditions: ApplicationConditions,
        startDate: LocalDate,
        endDate: LocalDate
    ): Application

    suspend fun findApplicationById(
        id: Int
    ): Application?

    suspend fun listApplications(): List<Application>

    suspend fun createApplicationAnswer(
        applicationId: Int,
        projectId: Int,
        answers: List<ApplicationItemAnswer>
    )

    suspend fun listNotAnsweredApplicationByProjectId(
        projectId: Int
    ): List<Application>

    suspend fun listAnswers(
        applicationId: Int
    ): List<ProjectsApplicationAnswer>
}