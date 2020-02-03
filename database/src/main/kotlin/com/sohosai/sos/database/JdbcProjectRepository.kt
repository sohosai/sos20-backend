package com.sohosai.sos.database

import com.sohosai.sos.domain.project.Project
import com.sohosai.sos.domain.project.ProjectAttribute
import com.sohosai.sos.domain.project.ProjectCategory
import com.sohosai.sos.domain.project.ProjectRepository
import kotlinx.coroutines.withContext
import kotliquery.Row
import kotliquery.queryOf
import kotliquery.sessionOf
import org.intellij.lang.annotations.Language
import java.util.*
import javax.sql.DataSource
import kotlin.coroutines.coroutineContext

@Language("sql")
private val CREATE_PROJECT_QUERY = """
    INSERT INTO projects(owner_id, sub_owner_id, name, kana_name, group_name, kana_group_name, description, category, attributes)
    VALUES (?, ?, ?, ?, ?, ?, ?, CAST(? AS project_category), CAST(? AS project_attribute[]))
    RETURNING id
""".trimIndent()

@Language("sql")
private val FIND_PROJECT_BY_OWNER_QUERY = """
    SELECT *
    FROM projects
    WHERE owner_id = ?
""".trimIndent()

@Language("sql")
private val LIST_PROJECTS_QUERY = """
    SELECT *
    FROM projects
""".trimIndent()

class JdbcProjectRepository(private val dataSource: DataSource) :
    ProjectRepository {
    override suspend fun createProject(
        ownerId: UUID,
        subOwnerId: UUID?,
        name: String,
        kanaName: String,
        groupName: String,
        kanaGroupName: String,
        description: String,
        category: ProjectCategory,
        attributes: List<ProjectAttribute>
    ): Project = withContext(coroutineContext) {
        sessionOf(dataSource).use { session ->
            session.single(
                queryOf(
                    CREATE_PROJECT_QUERY,
                    ownerId, subOwnerId, name, kanaName, groupName, kanaGroupName, description, category.name, attributes.map { it.name }.toTypedArray()
                ),
                projectExtractor
            )!!
        }
    }

    override suspend fun findProjectByOwner(ownerId: UUID): Project? = withContext(coroutineContext) {
        sessionOf(dataSource).use { session ->
            session.single(
                queryOf(
                    FIND_PROJECT_BY_OWNER_QUERY, ownerId
                ),
                projectExtractor
            )
        }
    }

    override suspend fun listProjects(): List<Project> = withContext(coroutineContext) {
        sessionOf(dataSource).use { session ->
            session.list(queryOf(LIST_PROJECTS_QUERY), projectExtractor)
        }
    }

    private val projectExtractor = { row: Row ->
        Project(
            id = row.int("id"),
            ownerId = row.uuid("owner_id"),
            subOwnerId = row.uuidOrNull("sub_owner_id"),
            name = row.string("name"),
            kanaName = row.string("kana_name"),
            groupName = row.string("group_name"),
            kanaGroupName = row.string("kana_group_name"),
            description = row.string("description"),
            category = ProjectCategory.valueOf(row.string("category")),
            attributes = row.array<String>("attributes").map {
                ProjectAttribute.valueOf(it)
            }
        )
    }
}