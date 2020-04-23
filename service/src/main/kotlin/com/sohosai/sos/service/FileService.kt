package com.sohosai.sos.service

import com.sohosai.sos.domain.file.StoredFile
import com.sohosai.sos.domain.file.UploadedFile
import com.sohosai.sos.domain.user.User

class FileService {
    suspend fun uploadFiles(files: List<UploadedFile>, uploader: User): List<StoredFile> {
        return files.map { it.store(uploader) }
    }
}