package com.sohosai.sos.presenter.controller

import com.sohosai.sos.domain.auth.AuthContext
import com.sohosai.sos.service.GraphQLService
import io.ktor.application.ApplicationCall
import io.ktor.auth.principal
import io.ktor.request.receive
import io.ktor.response.respond

class GraphQLController(private val graphQLService: GraphQLService) {
    suspend fun get(call: ApplicationCall) {
        call.respond(graphQLService.executeQuery(call.receive(), call.principal<AuthContext>()))
    }

    suspend fun post(call: ApplicationCall) {
        call.respond(graphQLService.executeQuery(call.receive(), call.principal<AuthContext>()))
    }
}