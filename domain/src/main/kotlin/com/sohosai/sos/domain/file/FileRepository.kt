package com.sohosai.sos.domain.file

import java.util.*

interface FileRepository {
    suspend fun storeFile(id: UUID, extension: String?, bytes: ByteArray)
}