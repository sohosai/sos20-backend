package com.sohosai.sos.infrastructure

import com.sohosai.sos.domain.graphql.GraphQLRequest
import graphql.*
import io.ktor.auth.AuthenticationFailedCause

private val allowedOperationWithoutToken = listOf(
    "IntrospectionQuery"
)

class GraphQLService(private val graphQL: GraphQL) {
    fun executeQuery(request: GraphQLRequest, authStatus: AuthStatus?): ExecutionResult {
        if (request.operationName !in allowedOperationWithoutToken) {
            if (authStatus == null) {
                // To support possible bug of ktor: https://github.com/ktorio/ktor/issues/1503
                return UnauthorizedExecutionResult("No token or something happened in server")
            } else if (authStatus is AuthStatus.Unauthorized) {
                return when (authStatus.cause) {
                    is AuthenticationFailedCause.NoCredentials -> UnauthorizedExecutionResult(
                        "No token"
                    )
                    is AuthenticationFailedCause.InvalidCredentials -> UnauthorizedExecutionResult(
                        "Invalid token"
                    )
                    is AuthenticationFailedCause.Error -> UnauthorizedExecutionResult(
                        authStatus.cause.cause
                    )
                }
            }
        }

        return graphQL.execute(
            ExecutionInput.newExecutionInput(request.query)
                .operationName(request.operationName ?: "")
                .variables(request.variables ?: emptyMap())
                .context(
                    (authStatus as? AuthStatus.Authorized)?.context
                )
        )
    }
}

private class UnauthorizedExecutionResult(private val reason: String) : ExecutionResult {
    override fun toSpecification(): MutableMap<String, Any> = mutableMapOf()

    override fun getErrors(): MutableList<GraphQLError> = mutableListOf(
        GraphqlErrorBuilder.newError()
            .message(reason)
            .build()
    )

    override fun isDataPresent(): Boolean = false

    override fun getExtensions(): MutableMap<Any, Any> = mutableMapOf()

    override fun <T : Any?> getData(): T = throw IllegalAccessException("Data not presented")

}