package com.sohosai.sos.service

import com.sohosai.sos.domain.file.StoredFile
import com.sohosai.sos.domain.file.UploadedFile

class FileService {
    suspend fun uploadFiles(files: List<UploadedFile>): List<StoredFile> {
        return files.map { it.store() }
    }
}