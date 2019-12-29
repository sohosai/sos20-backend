package com.sohosai.sos.infrastructure

import io.ktor.application.ApplicationCall
import io.ktor.auth.principal
import io.ktor.request.receive
import io.ktor.response.respond

class GraphQLController(private val graphQLService: GraphQLService) {
    suspend fun get(call: ApplicationCall) {
        call.respond(graphQLService.executeQuery(call.receive(), call.principal()))
    }

    suspend fun post(call: ApplicationCall) {
        call.respond(graphQLService.executeQuery(call.receive(), call.principal()))
    }
}