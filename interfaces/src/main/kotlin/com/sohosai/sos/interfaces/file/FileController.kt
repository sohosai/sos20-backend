package com.sohosai.sos.interfaces.file

import com.sohosai.sos.domain.file.StoredFile
import com.sohosai.sos.domain.file.UploadedFile
import com.sohosai.sos.service.FileService

class FileController(private val fileService: FileService) {
    suspend fun uploadFiles(files: List<UploadedFile>): List<StoredFile> {
        return fileService.uploadFiles(files)
    }
}