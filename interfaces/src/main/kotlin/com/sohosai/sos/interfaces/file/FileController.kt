package com.sohosai.sos.interfaces.file

import com.sohosai.sos.domain.file.Distribution
import com.sohosai.sos.domain.file.StoredFile
import com.sohosai.sos.domain.file.UploadedFile
import com.sohosai.sos.interfaces.AuthContext
import com.sohosai.sos.interfaces.toUser
import com.sohosai.sos.service.FileService
import java.io.File
import java.util.*

class FileController(private val fileService: FileService) {
    suspend fun uploadFiles(files: List<UploadedFile>, context: AuthContext): List<StoredFile> {
        return fileService.uploadFiles(files, context.toUser())
    }

    suspend fun distributeFiles(files: List<UploadedFile>, context: AuthContext): List<Distribution> {
        return fileService.distributeFiles(files, context.toUser())
    }

    suspend fun fetchDistribution(rawDistributionId: String, context: AuthContext): File {
        return fileService.fetchDistribution(
            distributionId = UUID.fromString(rawDistributionId),
            caller = context.toUser()
        )
    }
}