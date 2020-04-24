package com.sohosai.sos.service

import com.sohosai.sos.domain.file.*
import com.sohosai.sos.domain.project.ProjectRepository
import com.sohosai.sos.domain.user.Role
import com.sohosai.sos.domain.user.User
import com.sohosai.sos.service.exception.NotEnoughPermissionException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.util.*

// TODO: Optimize SQL from O(N) to O(1)
class FileService(
    private val distributionRepository: DistributionRepository,
    private val fileRepository: FileRepository
) {
    private val LOGGER = LoggerFactory.getLogger(FileService::class.java)

    suspend fun uploadFiles(files: List<UploadedFile>, uploader: User): List<StoredFile> {
        return files.map { it.store(uploader) }
    }

    suspend fun distributeFiles(files: List<UploadedFile>, uploader: User): List<Distribution> {
        if (!uploader.hasPrivilege(Role.COMMITTEE)) {
            throw NotEnoughPermissionException()
        }


        val storedFiles = files.map { it.store(uploader) }
        return storedFiles.map {
            val projectId = it.nameWithoutExtension().toIntOrNull()
                ?: throw IllegalArgumentException("Invalid project id: ${it.nameWithoutExtension()}")
            distributionRepository.createDistribution(it.id, projectId)
        }
    }

    suspend fun getDistribution(distributionId: UUID, caller: User): Distribution? {
        val dist = distributionRepository.findDistributionById(distributionId)
            ?: return null

        if (caller.owningProject()?.id != dist.projectId && caller.subOwningProject()?.id != dist.projectId && !caller.hasPrivilege(
                Role.COMMITTEE
            )
        ) {
            LOGGER.error("That user cannot access the distribution: user: ${caller.id}, distribution: $distributionId")
            return null
        }

        return dist
    }

    suspend fun fetchDistributionFile(distributionId: UUID, caller: User): File {
        val dist = distributionRepository.findDistributionById(distributionId)
            ?: throw IllegalArgumentException("Distribution not found: $distributionId")

        if (caller.owningProject()?.id != dist.projectId && caller.subOwningProject()?.id != dist.projectId) {
            throw IllegalArgumentException("That user cannot access the distribution: user: ${caller.id}, distribution: $distributionId")
        }

        return fileRepository.findFile(dist.fileId)
            ?: throw IllegalStateException("File not found for the distribution. file: ${dist.fileId}, distribution: $distributionId")
    }
}