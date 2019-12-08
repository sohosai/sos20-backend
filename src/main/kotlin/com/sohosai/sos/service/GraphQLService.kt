package com.sohosai.sos.service

import com.sohosai.sos.domain.graphql.GraphQLRequest
import graphql.ExecutionInput
import graphql.ExecutionResult
import graphql.GraphQL

class GraphQLService(private val graphQL: GraphQL) {
    fun executeQuery(request: GraphQLRequest): ExecutionResult {
        return graphQL.execute(
            ExecutionInput.newExecutionInput(request.query)
                .operationName(request.operationName ?: "")
                .variables(request.variables ?: emptyMap())
        )
    }
}