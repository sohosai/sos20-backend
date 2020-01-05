package com.sohosai.sos.database

import com.sohosai.sos.domain.project.Project
import com.sohosai.sos.domain.project.ProjectAttribute
import com.sohosai.sos.domain.project.ProjectCategory
import com.sohosai.sos.domain.user.User
import com.sohosai.sos.domain.project.ProjectRepository
import kotlinx.coroutines.withContext
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

class JdbcProjectRepository(private val dataSource: DataSource) :
    ProjectRepository {
    override suspend fun createProject(
        owner: User,
        subOwner: User?,
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
                    owner.id, subOwner?.id, name, kanaName, groupName, kanaGroupName, description, category.name, attributes.map { it.name }.toTypedArray()
                )
            ) { row ->
                Project(
                    id = row.uuid("id"),
                    ownerId = owner.id,
                    subOwnerId = subOwner?.id,
                    name = name,
                    kanaName = kanaName,
                    groupName = groupName,
                    kanaGroupName = kanaGroupName,
                    description = description,
                    category = category,
                    attributes = attributes
                )
            }!!
        }
    }

    override suspend fun findProjectByOwner(ownerId: UUID): Project? = withContext(coroutineContext) {
        sessionOf(dataSource).use { session ->
            session.single(
                queryOf(
                    FIND_PROJECT_BY_OWNER_QUERY, ownerId
                )
            ) { row ->
                Project(
                    id = row.uuid("id"),
                    ownerId = ownerId,
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
    }
}