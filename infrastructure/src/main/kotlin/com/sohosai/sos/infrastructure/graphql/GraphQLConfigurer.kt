package com.sohosai.sos.infrastructure.graphql

import com.coxautodev.graphql.tools.GraphQLResolver
import com.coxautodev.graphql.tools.SchemaParserBuilder
import com.coxautodev.graphql.tools.SchemaParserOptions
import com.coxautodev.graphql.tools.TypeDefinitionFactory
import graphql.*
import graphql.analysis.MaxQueryComplexityInstrumentation
import graphql.execution.*
import graphql.language.Definition
import graphql.language.ObjectTypeDefinition

object GraphQLConfigurer {
    fun configure(resolvers: List<GraphQLResolver<*>>): GraphQL {
        val options = SchemaParserOptions.newOptions()
            .typeDefinitionFactory(OutputTypeDefinitionFactory)
            .build()
        val schema = SchemaParserBuilder()
            .files(
                "graphql/base.gql",
                "graphql/user.gql",
                "graphql/project.gql"
            )
            .resolvers(resolvers)
            .options(options)
            .build().makeExecutableSchema()

        return GraphQL.newGraphQL(schema)
            .instrumentation(MaxQueryComplexityInstrumentation(100))
            .queryExecutionStrategy(AsyncExecutionStrategy(CustomDataFetcherExceptionHandler))
            .mutationExecutionStrategy(AsyncExecutionStrategy(CustomDataFetcherExceptionHandler))
            .build()
    }
}

private object OutputTypeDefinitionFactory : TypeDefinitionFactory {
    override fun create(existing: MutableList<Definition<*>>): MutableList<Definition<*>> {
        return existing.filterIsInstance<ObjectTypeDefinition>()
            .map { def ->
                def.transform { it.name(def.name + "Output") }
            }.toMutableList()
    }
}

private object CustomDataFetcherExceptionHandler : DataFetcherExceptionHandler {
    override fun onException(handlerParameters: DataFetcherExceptionHandlerParameters): DataFetcherExceptionHandlerResult {
        val error = GraphqlErrorBuilder.newError(handlerParameters.dataFetchingEnvironment)
            .message(handlerParameters.exception.message)
            .errorType(ErrorType.DataFetchingException)
            .path(handlerParameters.path)
            .location(null)
            .build()
        return DataFetcherExceptionHandlerResult.newResult(error).build()
    }

}