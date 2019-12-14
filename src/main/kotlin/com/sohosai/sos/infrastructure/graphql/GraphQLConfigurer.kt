package com.sohosai.sos.infrastructure.graphql

import com.coxautodev.graphql.tools.GraphQLResolver
import com.coxautodev.graphql.tools.SchemaParserBuilder
import graphql.GraphQL

object GraphQLConfigurer {
    fun configure(resolvers: List<GraphQLResolver<*>>): GraphQL {
        val schema = SchemaParserBuilder()
            .file("graphql/schema.gql")
            .resolvers(resolvers)
            .build().makeExecutableSchema()

        return GraphQL.newGraphQL(schema).build()
    }
}