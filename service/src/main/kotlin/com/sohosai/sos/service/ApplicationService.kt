package com.sohosai.sos.service

import com.sohosai.sos.domain.application.Application
import com.sohosai.sos.domain.application.ApplicationRepository
import com.sohosai.sos.domain.application.answer.ApplicationItemAnswer
import com.sohosai.sos.domain.application.answer.ProjectsApplicationAnswer
import com.sohosai.sos.domain.application.condition.ApplicationConditions
import com.sohosai.sos.domain.application.item.ApplicationItem
import com.sohosai.sos.domain.project.ProjectRepository
import com.sohosai.sos.domain.user.Role
import com.sohosai.sos.domain.user.User
import com.sohosai.sos.service.exception.NotEnoughPermissionException
import java.lang.IllegalStateException
import java.time.LocalDate

class ApplicationService(private val applicationRepository: ApplicationRepository, private val projectRepository: ProjectRepository) {
    suspend fun createApplication(
        name: String,
        description: String,
        items: List<ApplicationItem>,
        conditions: ApplicationConditions?,
        startDate: LocalDate,
        endDate: LocalDate,
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
            conditions = conditions,
            startDate = startDate,
            endDate = endDate
        )
    }

    suspend fun getApplication(
        id: Int
    ): Application? {
        return applicationRepository.findApplicationById(id)
    }

    suspend fun listApplications(
        caller: User
    ): List<Application> {
        if (!caller.hasPrivilege(Role.COMMITTEE)) {
            throw NotEnoughPermissionException()
        }

        return applicationRepository.listApplications()
    }

    suspend fun answerApplication(
        applicationId: Int,
        projectId: Int,
        answers: List<ApplicationItemAnswer>,
        caller: User
    ) {
        val project = projectRepository.findById(projectId) ?: throw IllegalArgumentException("The project with id $projectId not found.")
        if (project.ownerId != caller.id && project.subOwnerId != caller.id) {
            throw NotEnoughPermissionException()
        }

        val application = applicationRepository.findApplicationById(applicationId)
        requireNotNull(application) { "Application with id $applicationId is not found." }

        applicationRepository.findApplicationAnswerOfProject(applicationId, projectId)?.let {
            throw IllegalStateException("That project has already answered the application. project: $projectId application: $applicationId")
        }

        // throw error when required item is not answered
        application.items.forEach { item ->
            validateAnswer(item, answers.find { it.itemId == item.id })
        }

        applicationRepository.createApplicationAnswer(
            applicationId = applicationId,
            projectId = projectId,
            answers = answers
        )
    }

    suspend fun getApplicationAnswer(
        applicationId: Int,
        caller: User
    ): List<ProjectsApplicationAnswer> {
        if (!caller.hasPrivilege(Role.COMMITTEE)) {
            throw NotEnoughPermissionException()
        }

        return applicationRepository.listAnswers(applicationId)
    }

    private fun validateAnswer(item: ApplicationItem, answer: ApplicationItemAnswer?) {
        if (answer == null) {
            if (item.isRequired)
                throw IllegalArgumentException("No answer found for required application item with id ${item.id}")
            else
                return
        }
    }
}