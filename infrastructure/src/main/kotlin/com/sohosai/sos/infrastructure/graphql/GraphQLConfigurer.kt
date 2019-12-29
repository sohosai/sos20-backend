package com.sohosai.sos.infrastructure.graphql

import com.coxautodev.graphql.tools.GraphQLResolver
import com.coxautodev.graphql.tools.SchemaParserBuilder
import com.coxautodev.graphql.tools.SchemaParserOptions
import com.coxautodev.graphql.tools.TypeDefinitionFactory
import graphql.GraphQL
import graphql.analysis.MaxQueryComplexityInstrumentation
import graphql.language.Definition
import graphql.language.ObjectTypeDefinition
import kotlinx.coroutines.ExperimentalCoroutinesApi

object GraphQLConfigurer {
    fun configure(resolvers: List<GraphQLResolver<*>>): GraphQL {
        val options = SchemaParserOptions.newOptions()
            .typeDefinitionFactory(OutputTypeDefinitionFactory)
            .build()
        val schema = SchemaParserBuilder()
            .file("graphql/schema.gql")
            .resolvers(resolvers)
            .options(options)
            .build().makeExecutableSchema()

        return GraphQL.newGraphQL(schema)
            .instrumentation(MaxQueryComplexityInstrumentation(5))
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