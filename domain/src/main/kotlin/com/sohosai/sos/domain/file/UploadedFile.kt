package com.sohosai.sos.domain.file

import com.sohosai.sos.domain.KOIN
import com.sohosai.sos.domain.user.User
import java.util.*

private val fileRepository by KOIN.inject<FileRepository>()
private val fileInfoRepository by KOIN.inject<FileInfoRepository>()

data class UploadedFile(
    val name: String,
    val bytes: ByteArray

) {
    suspend fun store(uploader: User): StoredFile {
        val id = UUID.randomUUID()
        val extension = name.split(".").let {
            if (it.size > 1) it.last()
            else ""
        }

        fileInfoRepository.storeFileInfo(
            fileId = id,
            originalName = name,
            extension = extension,
            uploaderId = uploader.id
        )
        fileRepository.storeFile(id, bytes)

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