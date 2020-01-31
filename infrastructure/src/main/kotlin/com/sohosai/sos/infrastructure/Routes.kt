package com.sohosai.sos.infrastructure

import com.sohosai.sos.interfaces.user.UserController
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.principal
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import org.koin.ktor.ext.get

internal fun Routing.routes() {
    val userController = UserController(get())
    authenticate {
        post("/users") {
            call.respond(userController.createUser(
                input = call.receive(),
                context = call.principal<AuthStatus>().asContext()
            ))
        }
        get("/") { call.respond(HttpStatusCode.OK) }
        get("/health-check") { call.respond(HttpStatusCode.OK) }
    }
}