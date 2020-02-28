package com.sohosai.sos.database

import com.sohosai.sos.domain.user.*
import kotlinx.coroutines.withContext
import kotliquery.Row
import kotliquery.queryOf
import kotliquery.sessionOf
import org.intellij.lang.annotations.Language
import java.util.*
import javax.sql.DataSource
import kotlin.coroutines.coroutineContext

@Language("sql")
private val CREATE_USER_QUERY = """
    INSERT INTO users(name, kana_name, email, phone_number, student_id, affiliation_name, affiliation_type, role, auth_id)
    VALUES (?, ?, ?, ?, ?, ?, CAST(? AS affiliation_type), CAST(? AS user_role), ?)
    RETURNING id
""".trimIndent()

@Language("sql")
private val LIST_USERS_QUERY = """
    SELECT id, name, kana_name, email, phone_number, student_id, affiliation_name, affiliation_type, role
    FROM users
""".trimIndent()

@Language("sql")
private val FIND_USER_BY_ID_QUERY = """
    SELECT id, name, kana_name, email, phone_number, student_id, affiliation_name, affiliation_type, role
    FROM users
    WHERE id = ?
""".trimIndent()

@Language("sql")
private val FIND_USERS_BY_ID_QUERY = """
    SELECT id, name, kana_name, email, phone_number, student_id, affiliation_name, affiliation_type, role
    FROM users
    WHERE id IN (?)
""".trimIndent()

@Language("sql")
private val FIND_USER_BY_AUTH_ID_QUERY = """
    SELECT id, name, kana_name, email, phone_number, student_id, affiliation_name, affiliation_type, role
    FROM users
    WHERE auth_id = ?
""".trimIndent()

@Language("sql")
private val UPDATE_USER_ROLE_QUERY = """
    UPDATE users
    SET role = CAST(? AS user_role)
    WHERE id = ?
""".trimIndent()

class JdbcUserRepository(private val dataSource: DataSource) :
    UserRepository {

    override suspend fun createUser(
        name: String,
        kanaName: String,
        email: Email,
        phoneNumber: PhoneNumber,
        studentId: String,
        affiliationName: String,
        affiliationType: AffiliationType,
        role: Role,
        authId: String
    ): User = withContext(coroutineContext) {
        sessionOf(dataSource).use { session ->
            session.single(
                queryOf(
                    CREATE_USER_QUERY,
                    name, kanaName, email.value, phoneNumber.value, studentId, affiliationName, affiliationType.name, role.name, authId
                )
            ) {
                User(
                    id = UUID.nameUUIDFromBytes(it.bytes(1)),
                    name = name,
                    kanaName = kanaName,
                    email = email,
                    phoneNumber = phoneNumber,
                    studentId = studentId,
                    affiliation = Affiliation(affiliationName, affiliationType),
                    role = role
                )
            }!!
        }
    }

    override suspend fun listUsers(): List<User> = withContext(coroutineContext) {
        sessionOf(dataSource).use { session ->
            session.list(queryOf(LIST_USERS_QUERY), userExtractor)
        }
    }

    override suspend fun findUserById(id: UUID): User? = withContext(coroutineContext) {
        sessionOf(dataSource).use { session ->
            session.single(queryOf(FIND_USER_BY_ID_QUERY, id), userExtractor)
        }
    }

    override suspend fun findUsersById(ids: List<UUID>): List<User> = withContext(coroutineContext) {
        val query = FIND_USERS_BY_ID_QUERY.replace("?", ids.joinToString(", ") { "?" })
        sessionOf(dataSource).use { session ->
            session.list(queryOf(query, *ids.toTypedArray()), userExtractor)
        }
    }

    override suspend fun findUserByAuthId(authId: String): User? = withContext(coroutineContext) {
        sessionOf(dataSource).use { session ->
            session.single(queryOf(FIND_USER_BY_AUTH_ID_QUERY, authId), userExtractor)
        }
    }

    override suspend fun updateUserRole(userId: UUID, role: Role) = withContext<Unit>(coroutineContext) {
        sessionOf(dataSource).use { session ->
            session.execute(queryOf(UPDATE_USER_ROLE_QUERY, role.name, userId))
        }
    }

    private val userExtractor = { row: Row ->
        User(
            id = row.uuid("id"),
            name = row.string("name"),
            kanaName = row.string("kana_name"),
            email = Email(row.string("email")),
            phoneNumber = PhoneNumber(row.string("phone_number")),
            studentId = row.string("student_id"),
            affiliation = Affiliation(
                row.string("affiliation_name"),
                AffiliationType.valueOf(row.string("affiliation_type"))
            ),
            role = Role.valueOf(row.string("role"))
        )
    }
}