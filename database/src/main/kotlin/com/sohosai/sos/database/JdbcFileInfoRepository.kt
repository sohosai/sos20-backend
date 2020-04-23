package com.sohosai.sos.database

import com.sohosai.sos.domain.file.FileInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotliquery.queryOf
import kotliquery.sessionOf
import org.intellij.lang.annotations.Language
import java.util.*
import javax.sql.DataSource

@Language("sql")
private val STORE_FILE_INFO_QUERY = """
    INSERT INTO file_infos(id, original_name, ext, uploader_id)
    VALUES (?, ?, ?, ?)
""".trimIndent()

class JdbcFileInfoRepository(private val dataSource: DataSource) : FileInfoRepository {

    override suspend fun storeFileInfo(
        fileId: UUID,
        originalName: String,
        extension: String?,
        uploaderId: UUID
    ): Unit = withContext<Unit>(Dispatchers.IO) {
        sessionOf(dataSource).use { session ->
            session.execute(
                queryOf(
                    STORE_FILE_INFO_QUERY,
                    fileId, originalName, extension, uploaderId
                )
            )
        }
    }
}