package com.sohosai.sos.infrastructure

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.DataConversion
import io.ktor.gson.gson
import io.ktor.routing.routing
import io.ktor.server.netty.EngineMain
import org.koin.ktor.ext.Koin

fun main(args: Array<String>) {
    EngineMain.main(args)
}

@Suppress("unused") // called in application.conf
fun Application.configure() {
    install(Koin) {
        modules(KoinModules.base())
    }

    install(ContentNegotiation) {
        gson()
    }
    install(DataConversion)
    routing {
        routes()
    }
}