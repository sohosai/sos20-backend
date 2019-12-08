package com.sohosai.sos.infrastructure

import com.sohosai.sos.infrastructure.graphql.GraphQLConfigurer
import com.sohosai.sos.presenter.controller.GraphQLController
import com.sohosai.sos.service.GraphQLService
import org.koin.dsl.module

object KoinModules {
    fun base() = module {
        single { GraphQLConfigurer.configure() }
        single { GraphQLService(get()) }
        single { GraphQLController(get()) }
    }
}