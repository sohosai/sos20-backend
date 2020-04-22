package com.sohosai.sos.interfaces.application

import com.sohosai.sos.domain.application.answer.ProjectsApplicationAnswer
import com.sohosai.sos.domain.application.json.ApplicationJson
import com.sohosai.sos.interfaces.AuthContext
import com.sohosai.sos.interfaces.HttpStatusCodeException
import com.sohosai.sos.interfaces.toUser
import com.sohosai.sos.service.ApplicationService
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ApplicationController(private val applicationService: ApplicationService) {
    suspend fun createApplication(input: CreateApplicationInput, context: AuthContext): ApplicationJson {
        val items = input.items.map { it.toApplicationItem() }
        val conditions = input.conditions?.toApplicationConditions()

        val application = applicationService.createApplication(
            name = input.name,
            description = input.description,
            items = items,
            conditions = conditions,
            startDate = LocalDate.parse(input.startDate, DateTimeFormatter.ISO_DATE),
            endDate = LocalDate.parse(input.endDate, DateTimeFormatter.ISO_DATE),
            author = context.toUser()
        )

        return ApplicationJson.fromApplication(application)
    }

    suspend fun getApplication(rawId: String): ApplicationJson {
        val application = applicationService.getApplication(
            id = rawId.toInt()
        ) ?: throw HttpStatusCodeException(404, "Application not found. applicationId: $rawId")

        return ApplicationJson.fromApplication(application)
    }

    suspend fun listApplications(context: AuthContext): List<ApplicationJson> {
        val applications = applicationService.listApplications(
            caller = context.toUser()
        )

        return applications.map { ApplicationJson.fromApplication(it) }
    }

    suspend fun answerApplication(input: AnswerApplicationInput, rawApplicationId: String, context: AuthContext) {
        val answers = input.answers.map { it.toApplicationItemAnswer() }

        applicationService.answerApplication(
            applicationId = rawApplicationId.toInt(),
            projectId = input.projectId,
            answers = answers,
            caller = context.toUser()
        )
    }

    suspend fun listAnswers(rawApplicationId: String, context: AuthContext): List<ProjectsApplicationAnswer> {
        return applicationService.getApplicationAnswer(
            applicationId = rawApplicationId.toInt(),
            caller = context.toUser()
        )
    }
}