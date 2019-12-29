package com.sohosai.sos.infrastructure

import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import org.koin.ktor.ext.get

internal fun Routing.routes() {
    val graphQLController: GraphQLController = get()
    authenticate(optional = true) {
        get("/graphql") { graphQLController.get(call) }
        post("/graphql") { graphQLController.post(call) }
        get("/") { call.respond(HttpStatusCode.OK) }
        get("/health-check") { call.respond(HttpStatusCode.OK) }
    }
}