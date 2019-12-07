package com.sohosai.sos

import io.ktor.application.Application
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.configure() {
    println("Hello world")
}
