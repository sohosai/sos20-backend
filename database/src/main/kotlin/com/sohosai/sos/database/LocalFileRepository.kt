package com.sohosai.sos.database

import com.sohosai.sos.domain.file.FileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

class LocalFileRepository : FileRepository {
    override suspend fun storeFile(id: UUID, bytes: ByteArray): Unit = withContext<Unit>(Dispatchers.IO) {
        initDir()

        File("./uploaded/${id}").apply {
            createNewFile()
            writeBytes(bytes)
        }
    }

    override suspend fun findFile(id: UUID): File? = withContext(Dispatchers.IO) {
        val file = File("./uploaded/${id}")
        if (file.exists()) file else null
    }

    private fun initDir() {
        File("./uploaded").apply {
            if (!exists()) mkdir()
        }
    }
}