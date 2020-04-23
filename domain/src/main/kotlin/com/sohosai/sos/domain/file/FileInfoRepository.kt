package com.sohosai.sos.domain.file

import java.util.*

interface FileInfoRepository {
    suspend fun storeFileInfo(
        fileId: UUID,
        originalName: String,
        extension: String?,
        uploaderId: UUID
    )
}