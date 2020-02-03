package com.sohosai.sos.infrastructure

import com.sohosai.sos.interfaces.project.ProjectController
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
import io.ktor.routing.route
import io.ktor.util.getOrFail
import org.koin.ktor.ext.get

internal fun Routing.routes() {
    val userController = UserController(get())
    val projectController = ProjectController(get())
    authenticate {
        route("/users") {
            get {
                call.respond(userController.listUsers(
                    context = call.principal<AuthStatus>().asContext()
                ))
            }
            post {
                call.respond(HttpStatusCode.Created, userController.createUser(
                    input = call.receive(),
                    context = call.principal<AuthStatus>().asContext()
                ))
            }
            get("/{id}") {
                call.respond(userController.getUser(
                    rawUserId = call.parameters.getOrFail("id"),
                    context = call.principal<AuthStatus>().asContext()
                ))
            }
            get("/login") {
                call.respond(userController.loginUser(
                    context = call.principal<AuthStatus>().asContext()
                ))
            }
        }
        route ("/projects") {
            get {
                call.respond(projectController.listProjects(
                    context = call.principal<AuthStatus>().asContext()
                ))
            }
            post {
                call.respond(HttpStatusCode.Created, projectController.createProject(
                    input = call.receive(),
                    context = call.principal<AuthStatus>().asContext()
                ))
            }
        }
        get("/") { call.respond(HttpStatusCode.OK) }
        get("/health-check") { call.respond(HttpStatusCode.OK) }
    }
}