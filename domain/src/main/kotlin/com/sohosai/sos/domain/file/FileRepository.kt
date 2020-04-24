package com.sohosai.sos.domain.file

import java.io.File
import java.util.*

interface FileRepository {
    suspend fun storeFile(id: UUID, bytes: ByteArray)
    suspend fun findFile(id: UUID): File?
}