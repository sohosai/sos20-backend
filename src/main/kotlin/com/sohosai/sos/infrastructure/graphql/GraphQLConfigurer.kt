package com.sohosai.sos.infrastructure.graphql

import com.coxautodev.graphql.tools.SchemaParserBuilder
import com.sohosai.sos.presenter.resolver.HelloQueryResolver
import graphql.GraphQL

object GraphQLConfigurer {
    fun configure(): GraphQL {
        val schema = SchemaParserBuilder()
            .file("graphql/schema.gql")
            .resolvers(
                HelloQueryResolver()
            )
            .build().makeExecutableSchema()

        return GraphQL.newGraphQL(schema).build()
    }
}