package com.sohosai.sos.infrastructure.repository.jdbc

import com.sohosai.sos.domain.user.*
import kotlinx.coroutines.withContext
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
}