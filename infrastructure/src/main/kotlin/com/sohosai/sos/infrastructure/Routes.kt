package com.sohosai.sos.infrastructure

import com.sohosai.sos.domain.file.UploadedFile
import com.sohosai.sos.interfaces.application.ApplicationController
import com.sohosai.sos.interfaces.file.FileController
import com.sohosai.sos.interfaces.project.ProjectController
import com.sohosai.sos.interfaces.user.UserController
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.principal
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.readAllParts
import io.ktor.http.content.streamProvider
import io.ktor.request.receive
import io.ktor.request.receiveMultipart
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.response.respondFile
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.util.getOrFail
import org.koin.ktor.ext.get

internal fun Routing.routes() {
    val userController = UserController(get())
    val projectController = ProjectController(get())
    val applicationController = ApplicationController(get())
    val fileController = FileController(get())
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
            route("/{id}") {
                get {
                    call.respond(
                        userController.getUser(
                            rawUserId = call.parameters.getOrFail("id"),
                            context = call.principal<AuthStatus>().asContext()
                        )
                    )
                }
                get("/project") {
                    userController.getOwningProject(
                        context = call.principal<AuthStatus>().asContext()
                    )?.let {
                        call.respond(it)
                    } ?: call.respond(HttpStatusCode.NoContent)
                }
                get("/subown-project") {
                    userController.getSubOwningProject(
                        context = call.principal<AuthStatus>().asContext()
                    )?.let {
                        call.respond(it)
                    } ?: call.respond(HttpStatusCode.NoContent)
                }
                post("/role") {
                    call.respond(userController.updateUserRole(
                        rawUserId = call.parameters.getOrFail("id"),
                        rawRole = call.receiveText(),
                        context = call.principal<AuthStatus>().asContext()
                    ))
                }
            }
            get("/login") {
                call.respond(userController.loginUser(
                    context = call.principal<AuthStatus>().asContext()
                ))
            }
        }
        route("/projects") {
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
            route("/{id}") {
                get {
                    call.respond(
                        projectController.getProject(
                            rawId = call.parameters.getOrFail("id"),
                            context = call.principal<AuthStatus>().asContext()
                        )
                    )
                }
                get("/members") {
                    call.respond(
                        projectController.getProjectMembers(
                            rawId = call.parameters.getOrFail("id"),
                            context = call.principal<AuthStatus>().asContext()
                        )
                    )
                }
                route("/applications") {
                    get("/answered") {
                        call.respond(
                            projectController.getAnsweredApplications(
                                rawProjectId = call.parameters.getOrFail("id"),
                                context = call.principal<AuthStatus>().asContext()
                            )
                        )
                    }
                    get("/not-answered") {
                        call.respond(
                            projectController.getNotAnsweredApplications(
                                rawProjectId = call.parameters.getOrFail("id"),
                                context = call.principal<AuthStatus>().asContext()
                            )
                        )
                    }
                }
            }
        }
        route("/applications") {
            get {
                call.respond(applicationController.listApplications(
                    context = call.principal<AuthStatus>().asContext()
                ))
            }
            post {
                call.respond(applicationController.createApplication(
                    input = call.receive(),
                    context = call.principal<AuthStatus>().asContext()
                ))
            }
            route("/{id}") {
                get {
                    call.respond(applicationController.getApplication(
                        rawId = call.parameters.getOrFail("id")
                    ))
                }
                get("/answers") {
                    call.respond(applicationController.listAnswers(
                        rawApplicationId = call.parameters.getOrFail(("id")),
                        context = call.principal<AuthStatus>().asContext()
                    ))
                }
                post("/answers") {
                    call.respond(HttpStatusCode.Created, applicationController.answerApplication(
                        input = call.receive(),
                        rawApplicationId = call.parameters.getOrFail("id"),
                        context = call.principal<AuthStatus>().asContext()
                    ))
                }
            }
        }
        route("/files") {
            post("/") {
                call.respond(HttpStatusCode.Created, fileController.uploadFiles(
                    files = call.receiveUploadedFiles(),
                    context = call.principal<AuthStatus>().asContext()
                ))
            }
        }
        route("distributions") {
            post("/") {
                call.respond(HttpStatusCode.Created, fileController.distributeFiles(
                    files = call.receiveUploadedFiles(),
                    context = call.principal<AuthStatus>().asContext()
                ))
            }
            get("/{id}") {
                call.respondFile(fileController.fetchDistribution(
                    rawDistributionId = call.parameters.getOrFail("id"),
                    context = call.principal<AuthStatus>().asContext()
                ))
            }
        }
        get("/") { call.respond(HttpStatusCode.OK) }
        get("/health-check") { call.respond(HttpStatusCode.OK) }
    }
}

suspend fun ApplicationCall.receiveUploadedFiles(): List<UploadedFile> {
    return this.receiveMultipart().readAllParts()
        .filterIsInstance<PartData.FileItem>()
        .map {
            UploadedFile(
                name = it.name ?: "",
                bytes = it.streamProvider().readBytes()
            )
        }
}