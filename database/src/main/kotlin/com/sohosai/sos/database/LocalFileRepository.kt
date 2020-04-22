package com.sohosai.sos.database

import com.sohosai.sos.domain.file.FileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

class LocalFileRepository : FileRepository {
    override suspend fun storeFile(id: UUID, extension: String?, bytes: ByteArray): Unit = withContext<Unit>(Dispatchers.IO) {
        initDir()

        val extensionWithDot = extension?.let { ".${extension}" } ?: ""
        File("./uploaded/${id}${extensionWithDot}").apply {
            createNewFile()
            writeBytes(bytes)
        }
    }

    private fun initDir() {
        File("./uploaded").apply {
            if (!exists()) mkdir()
        }
    }
}