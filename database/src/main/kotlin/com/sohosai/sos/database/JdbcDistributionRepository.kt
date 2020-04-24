package com.sohosai.sos.database

import com.sohosai.sos.domain.file.Distribution
import com.sohosai.sos.domain.file.DistributionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotliquery.queryOf
import kotliquery.sessionOf
import org.intellij.lang.annotations.Language
import java.util.*
import javax.sql.DataSource

@Language("sql")
private val CREATE_DISTRIBUTION_QUERY = """
    INSERT INTO distributions(id, file_id, project_id)
    VALUES (?, ?, ?)
""".trimIndent()

@Language("sql")
private val FIND_DISTRIBUTION_QUERY = """
    SELECT id, file_id, project_id
    FROM distributions
    WHERE id = ?
""".trimIndent()

class JdbcDistributionRepository(private val dataSource: DataSource) : DistributionRepository {

    override suspend fun createDistribution(fileId: UUID, projectId: Int): Distribution = withContext(Dispatchers.IO) {
        val id = UUID.randomUUID()
        sessionOf(dataSource).use { session ->
            session.execute(
                queryOf(CREATE_DISTRIBUTION_QUERY, id, fileId, projectId)
            )
        }
        Distribution(
            id = id,
            fileId = fileId,
            projectId = projectId
        )
    }

    override suspend fun findDistributionById(id: UUID): Distribution? = withContext(Dispatchers.IO) {
        sessionOf(dataSource).use { session ->
            session.single(
                queryOf(FIND_DISTRIBUTION_QUERY, id)
            ) {row ->
                Distribution(
                    id = row.uuid("id"),
                    fileId = row.uuid("file_id"),
                    projectId = row.int("project_id")
                )
            }
        }
    }
}