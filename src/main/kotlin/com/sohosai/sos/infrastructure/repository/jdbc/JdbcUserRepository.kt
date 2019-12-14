package com.sohosai.sos.infrastructure.repository.jdbc

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
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
    RETURNING id
""".trimIndent()

@Language("sql")
private val LIST_USERS_QUERY = """
    SELECT id, name, kana_name, email, phone_number, student_id, affiliation_name, affiliation_type, role
    FROM users
""".trimIndent()

@Language("sql")
private val FIND_USER_BY_AUTH_ID_QUERY = """
    SELECT id, name, kana_name, email, phone_number, student_id, affiliation_name, affiliation_type, role
    FROM users
    WHERE auth_id = ?
""".trimIndent()

class JdbcUserRepository(private val dataSource: DataSource) : UserRepository {

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
                    name, kanaName, email.value, phoneNumber.value, studentId, affiliationName, affiliationType.ordinal, role.ordinal, authId
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

    override suspend fun findUserByAuthId(authId: String): User? = withContext(coroutineContext) {
        sessionOf(dataSource).use { session ->
            session.single(queryOf(FIND_USER_BY_AUTH_ID_QUERY, authId), userExtractor)
        }
    }

    private val userExtractor = { row: Row ->
        User(
            id = row.uuid(1),
            name = row.string(2),
            kanaName = row.string(3),
            email = Email(row.string(4)),
            phoneNumber = PhoneNumber(row.string(5)),
            studentId = row.string(6),
            affiliation = Affiliation(
                row.string(7),
                AffiliationType.values()[row.int(8)]
            ),
            role = Role.values()[row.int(9)]
        )
    }
}