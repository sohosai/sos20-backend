package com.sohosai.sos.presenter.resolver

import com.coxautodev.graphql.tools.GraphQLQueryResolver

@Suppress("unused")
class HelloQueryResolver : GraphQLQueryResolver {
    fun hello(name: String): String {
        return "Hello ${name}!"
    }
}