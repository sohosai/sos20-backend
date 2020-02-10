package com.sohosai.sos.service

import com.sohosai.sos.domain.application.Application
import com.sohosai.sos.domain.application.ApplicationRepository
import com.sohosai.sos.domain.application.condition.ApplicationConditions
import com.sohosai.sos.domain.application.item.ApplicationItem
import com.sohosai.sos.domain.user.Role
import com.sohosai.sos.domain.user.User
import com.sohosai.sos.service.exception.NotEnoughPermissionException

class ApplicationService(private val applicationRepository: ApplicationRepository) {
    suspend fun createApplication(
        name: String,
        description: String,
        items: List<ApplicationItem>,
        conditions: ApplicationConditions,
        author: User
    ): Application {
        if (!author.hasPrivilege(Role.COMMITTEE)) {
            throw NotEnoughPermissionException()
        }

        return applicationRepository.createApplication(
            name = name,
            description = description,
            authorId = author.id,
            items = items,
            conditions = conditions
        )
    }
}