package com.sohosai.sos.interfaces.application

import com.sohosai.sos.domain.application.Application
import com.sohosai.sos.interfaces.AuthContext
import com.sohosai.sos.interfaces.HttpStatusCodeException
import com.sohosai.sos.interfaces.toUser
import com.sohosai.sos.service.ApplicationService

class ApplicationController(private val applicationService: ApplicationService) {
    suspend fun createApplication(input: CreateApplicationInput, context: AuthContext): Application {
        val items = input.items.map { it.toApplicationItem() }
        val conditions = input.conditions.toApplicationConditions()

        return applicationService.createApplication(input.name, input.description, items, conditions, context.toUser())
    }

    suspend fun getApplication(rawId: String): Application {
        return applicationService.getApplication(
            id = rawId.toInt()
        ) ?: throw HttpStatusCodeException(404, "Application not found. applicationId: $rawId")
    }

    suspend fun listApplications(context: AuthContext): List<Application> {
        return applicationService.listApplications(
            caller = context.toUser()
        )
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
}