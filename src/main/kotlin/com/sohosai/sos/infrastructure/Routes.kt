package com.sohosai.sos.infrastructure

import com.sohosai.sos.presenter.controller.GraphQLController
import io.ktor.application.call
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import org.koin.ktor.ext.get

internal fun Routing.routes() {
    val graphQLController: GraphQLController = get()

    get("/graphql") { graphQLController.get(call) }
    post("/graphql") { graphQLController.post(call) }
}