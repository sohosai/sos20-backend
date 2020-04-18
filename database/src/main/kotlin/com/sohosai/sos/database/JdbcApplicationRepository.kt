package com.sohosai.sos.database

import com.sohosai.sos.domain.application.Application
import com.sohosai.sos.domain.application.ApplicationRepository
import com.sohosai.sos.domain.application.answer.ApplicationItemAnswer
import com.sohosai.sos.domain.application.answer.ProjectsApplicationAnswer
import com.sohosai.sos.domain.application.condition.ApplicationConditions
import com.sohosai.sos.domain.application.item.ApplicationItem
import com.sohosai.sos.domain.application.json.ApplicationConditionsJson
import com.sohosai.sos.domain.application.json.ApplicationItemAnswerJson
import com.sohosai.sos.domain.application.json.ApplicationItemJson
import com.sohosai.sos.domain.project.Project
import com.sohosai.sos.domain.project.ProjectAttribute
import com.sohosai.sos.domain.project.ProjectCategory
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.withContext
import kotliquery.Row
import kotliquery.queryOf
import kotliquery.sessionOf
import org.intellij.lang.annotations.Language
import java.time.LocalDate
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
    INSERT INTO applications(name, description, author_id, items, conditions, start_date, end_date)
    VALUES (?, ?, ?, CAST(? AS jsonb), CAST(? AS jsonb), ?, ?)
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

@Language("sql")
private val LIST_NOT_ANSWERED_APPLICATIONS_BY_PROJECT_ID_QUERY = """
    SELECT applications.*
    FROM applications
        LEFT JOIN application_answers answers on applications.id = answers.application_id AND project_id = ?
    WHERE answers.application_id IS NULL
""".trimIndent()

@Language("sql")
private val LIST_ANSWERS_QUERY = """
    SELECT application_answers.*, p.*
    FROM application_answers
        LEFT JOIN projects AS p on application_answers.project_id = p.id
    WHERE application_id = ?
""".trimIndent()

class JdbcApplicationRepository(private val dataSource: DataSource) : ApplicationRepository {
    override suspend fun createApplication(
        name: String,
        description: String,
        authorId: UUID,
        items: List<ApplicationItem>,
        conditions: ApplicationConditions?,
        startDate: LocalDate,
        endDate: LocalDate
    ): Application = withContext(coroutineContext) {
        val itemsJson = itemsAdapter.toJson(items.map { ApplicationItemJson.fromApplicationItem(it) })
        val conditionsJson = conditions?.let {
            conditionsAdapter.toJson(ApplicationConditionsJson.fromApplicationConditions(it))
        }

        sessionOf(dataSource).use { session ->
            session.single(
                queryOf(
                    CREATE_APPLICATION_QUERY,
                    name, description, authorId, itemsJson, conditionsJson, startDate, endDate
                )
            ) { row ->
                Application(
                    id = row.int("id"),
                    name = name,
                    description = description,
                    authorId = authorId,
                    items = items,
                    conditions = conditions,
                    startDate = startDate,
                    endDate = endDate
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

    override suspend fun listNotAnsweredApplicationByProjectId(
        projectId: Int
    ): List<Application> = withContext(coroutineContext) {
        sessionOf(dataSource).use { session ->
            session.list(
                queryOf(
                    LIST_NOT_ANSWERED_APPLICATIONS_BY_PROJECT_ID_QUERY,
                    projectId
                ),
                applicationExtractor
            )
        }
    }

    override suspend fun listAnswers(
        applicationId: Int
    ): List<ProjectsApplicationAnswer> = withContext(coroutineContext) {
        sessionOf(dataSource).use { session ->
            session.list(queryOf(LIST_ANSWERS_QUERY, applicationId)) { row ->
                ProjectsApplicationAnswer(
                    project = Project(
                        id = row.int("id"),
                        ownerId = row.uuid("owner_id"),
                        subOwnerId = row.uuidOrNull("sub_owner_id"),
                        name = row.string("name"),
                        kanaName = row.string("kana_name"),
                        groupName = row.string("group_name"),
                        kanaGroupName = row.string("kana_group_name"),
                        description = row.string("description"),
                        category = ProjectCategory.valueOf(row.string("category")),
                        attributes = row.array<String>("attributes").map { ProjectAttribute.valueOf(it) }
                    ),
                    answers = answersAdapter.fromJson(row.string("answers"))!!
                )
            }
        }
    }

    private val applicationExtractor = { row: Row ->
        Application(
            id = row.int("id"),
            name = row.string("name"),
            description = row.string("description"),
            authorId = row.uuid("author_id"),
            items = itemsAdapter.fromJson(row.string("items"))!!.map { it.toApplicationItem() },
            conditions = row.stringOrNull("conditions")?.let {
                conditionsAdapter.fromJson(it)!!.toApplicationConditions()
            },
            startDate = row.localDate("start_date"),
            endDate = row.localDate("end_date")
        )
    }
}