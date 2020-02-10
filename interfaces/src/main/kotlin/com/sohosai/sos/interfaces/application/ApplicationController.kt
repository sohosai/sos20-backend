package com.sohosai.sos.interfaces.application

import com.sohosai.sos.domain.application.Application
import com.sohosai.sos.interfaces.AuthContext
import com.sohosai.sos.interfaces.toUser
import com.sohosai.sos.service.ApplicationService

class ApplicationController(private val applicationService: ApplicationService) {
    suspend fun createApplication(input: CreateApplicationInput, context: AuthContext): Application {
        val items = input.items.map { it.toApplicationItem() }
        val conditions = input.conditions.toApplicationConditions()

        return applicationService.createApplication(input.name, input.description, items, conditions, context.toUser())
    }
}