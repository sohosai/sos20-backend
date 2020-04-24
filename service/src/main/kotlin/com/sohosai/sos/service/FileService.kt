package com.sohosai.sos.service

import com.sohosai.sos.domain.file.Distribution
import com.sohosai.sos.domain.file.DistributionRepository
import com.sohosai.sos.domain.file.StoredFile
import com.sohosai.sos.domain.file.UploadedFile
import com.sohosai.sos.domain.user.Role
import com.sohosai.sos.domain.user.User
import com.sohosai.sos.service.exception.NotEnoughPermissionException
import java.lang.IllegalArgumentException

// TODO: Optimize SQL from O(N) to O(1)
class FileService(private val distributionRepository: DistributionRepository) {
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
}