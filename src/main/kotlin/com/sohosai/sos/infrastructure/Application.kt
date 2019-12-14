package com.sohosai.sos.infrastructure

import com.sohosai.sos.domain.auth.AuthContext
import com.sohosai.sos.domain.user.Email
import io.ktor.application.Application
import io.ktor.application.ApplicationEnvironment
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.jwt
import io.ktor.features.ContentNegotiation
import io.ktor.features.DataConversion
import io.ktor.gson.gson
import io.ktor.routing.routing
import io.ktor.server.netty.EngineMain
import org.flywaydb.core.Flyway
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.get
import javax.sql.DataSource
import kotlin.properties.Delegates

var env: ApplicationEnvironment by Delegates.notNull()

fun main(args: Array<String>) {
    EngineMain.main(args)
}

@Suppress("unused") // called in application.conf
fun Application.configure() {
    env = this.environment

    install(Koin) {
        modules(KoinModules.base())
    }
    install(Authentication) {
        jwt {
            realm = "api"
            // TODO: verify audience (client id)
            verifier(JWTConfig.jwkProvider, "https://tsukuba.auth0.com/")
            validate { credential ->
                credential.payload.getClaim("email")?.asString()?.let {
                    AuthContext(Email(it), credential.payload.getClaim("email_verified").asBoolean())
                }
            }
        }
    }
    install(ContentNegotiation) {
        gson()
    }
    install(DataConversion)

    migrateDatabase(get())

    routing {
        routes()
    }
}

private fun migrateDatabase(dataSource: DataSource) {
    Flyway.configure().apply {
        dataSource(dataSource)
        locations("db/migration")
    }.load().migrate()
}