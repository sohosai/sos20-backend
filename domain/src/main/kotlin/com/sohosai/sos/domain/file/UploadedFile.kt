package com.sohosai.sos.domain.file

import com.sohosai.sos.domain.KOIN
import java.util.*

private val fileRepository by KOIN.inject<FileRepository>()

data class UploadedFile(
    val name: String,
    val bytes: ByteArray

) {
    suspend fun store(): StoredFile {
        val id = UUID.randomUUID()
        val extension = name.split(".").let {
            if (it.size > 1) it.last()
            else ""
        }

        fileRepository.storeFile(id, extension, bytes)

        return StoredFile(
            id = id,
            name = name
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UploadedFile

        if (name != other.name) return false
        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + bytes.contentHashCode()
        return result
    }
}