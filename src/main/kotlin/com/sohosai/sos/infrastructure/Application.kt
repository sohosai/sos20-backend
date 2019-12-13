package com.sohosai.sos.infrastructure

import io.ktor.application.Application
import io.ktor.application.ApplicationEnvironment
import io.ktor.application.install
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