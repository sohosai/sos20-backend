package com.sohosai.sos.database

import com.sohosai.sos.domain.application.Application
import com.sohosai.sos.domain.application.ApplicationRepository
import com.sohosai.sos.domain.application.answer.ApplicationItemAnswer
import com.sohosai.sos.domain.application.condition.ApplicationConditions
import com.sohosai.sos.domain.application.item.ApplicationItem
import com.sohosai.sos.domain.application.json.ApplicationConditionsJson
import com.sohosai.sos.domain.application.json.ApplicationItemAnswerJson
import com.sohosai.sos.domain.application.json.ApplicationItemJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.withContext
import kotliquery.Row
import kotliquery.queryOf
import kotliquery.sessionOf
import org.intellij.lang.annotations.Language
import java.util.*
import javax.sql.DataSource
import kotlin.coroutines.coroutineContext

// TODO: DI
private val moshi = Moshi.Builder().apply {
    add(KotlinJsonAdapterFactory())
}.build()
private val itemsAdapter: JsonAdapter<List<ApplicationItemJson>> = moshi.adapter(Types.newParameterizedType(List::class.java, ApplicationItemJson::class.java))
private val conditionsAdapter: JsonAdapter<ApplicationConditionsJson> = moshi.adapter(ApplicationConditionsJson::class.java)
private val answersAdapter: JsonAdapter<List<ApplicationItemAnswerJson>> = moshi.adapter(Types.newParameterizedType(List::class.java, ApplicationItemAnswerJson::class.java))

@Language("sql")
private val CREATE_APPLICATION_QUERY = """
    INSERT INTO applications(name, description, author_id, items, conditions)
    VALUES (?, ?, ?, CAST(? AS jsonb), CAST(? AS jsonb))
    RETURNING id
""".trimIndent()

@Language("sql")
private val FIND_APPLICATION_BY_ID_QUERY = """
    SELECT *
    FROM applications
    WHERE id = ?
""".trimIndent()

@Language("sql")
private val LIST_APPLICATIONS_QUERY = """
    SELECT *
    FROM applications
""".trimIndent()

@Language("sql")
private val CREATE_APPLICATION_ANSWER_QUERY = """
    INSERT INTO application_answers(application_id, project_id, answers)
    VALUES (?, ?, CAST(? as jsonb))
""".trimIndent()

class JdbcApplicationRepository(private val dataSource: DataSource) : ApplicationRepository {
    override suspend fun createApplication(
        name: String,
        description: String,
        authorId: UUID,
        items: List<ApplicationItem>,
        conditions: ApplicationConditions
    ): Application = withContext(coroutineContext) {
        val itemsJson = itemsAdapter.toJson(items.map { ApplicationItemJson.fromApplicationItem(it) })
        val conditionsJson = conditionsAdapter.toJson(ApplicationConditionsJson.fromApplicationConditions(conditions))

        sessionOf(dataSource).use { session ->
            session.single(
                queryOf(
                    CREATE_APPLICATION_QUERY,
                    name, description, authorId, itemsJson, conditionsJson
                )
            ) { row ->
                Application(
                    id = row.int("id"),
                    name = name,
                    description = description,
                    authorId = authorId,
                    items = items,
                    conditions = conditions
                )
            }!!
        }
    }

    override suspend fun findApplicationById(
        id: Int
    ): Application? = withContext(coroutineContext) {
        sessionOf(dataSource).use { session ->
            session.single(
                queryOf(FIND_APPLICATION_BY_ID_QUERY, id), applicationExtractor
            )
        }
    }

    override suspend fun listApplications(): List<Application> = withContext(coroutineContext) {
        sessionOf(dataSource).use { session ->
            session.list(
                queryOf(LIST_APPLICATIONS_QUERY), applicationExtractor
            )
        }
    }

    override suspend fun createApplicationAnswer(
        applicationId: Int,
        projectId: Int,
        answers: List<ApplicationItemAnswer>
    ): Unit = withContext<Unit>(coroutineContext) {
        val answersJson = answersAdapter.toJson(answers.map { ApplicationItemAnswerJson.fromApplicationItemAnswer(it) })

        sessionOf(dataSource).use { session ->
            session.execute(
                queryOf(
                    CREATE_APPLICATION_ANSWER_QUERY,
                    applicationId, projectId, answersJson
                )
            )
        }
    }

    private val applicationExtractor = { row: Row ->
        Application(
            id = row.int("id"),
            name = row.string("name"),
            description = row.string("description"),
            authorId = row.uuid("author_id"),
            items = itemsAdapter.fromJson(row.string("items"))!!.map { it.toApplicationItem() },
            conditions = conditionsAdapter.fromJson(row.string("conditions"))!!.toApplicationConditions()
        )
    }
}