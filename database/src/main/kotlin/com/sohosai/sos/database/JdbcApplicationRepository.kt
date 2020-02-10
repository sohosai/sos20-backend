package com.sohosai.sos.database

import com.sohosai.sos.domain.application.Application
import com.sohosai.sos.domain.application.ApplicationRepository
import com.sohosai.sos.domain.application.condition.ApplicationConditions
import com.sohosai.sos.domain.application.item.ApplicationItem
import com.sohosai.sos.domain.application.json.ApplicationConditionsJson
import com.sohosai.sos.domain.application.json.ApplicationItemJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.withContext
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

@Language("sql")
private val CREATE_APPLICATION_QUERY = """
    INSERT INTO applications(name, description, author_id, items, conditions)
    VALUES (?, ?, ?, CAST(? AS jsonb), CAST(? AS jsonb))
    RETURNING id
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
}