package com.sohosai.sos

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>) {
    EngineMain.main(args)
}

@Suppress("unused") // called in application.conf
fun Application.configure() {
    routing {
        get("/") {
            call.respond("Hello world.")
        }
    }
}
