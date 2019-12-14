package com.sohosai.sos.infrastructure

import com.coxautodev.graphql.tools.GraphQLResolver
import com.sohosai.sos.domain.user.UserRepository
import com.sohosai.sos.infrastructure.graphql.GraphQLConfigurer
import com.sohosai.sos.infrastructure.repository.jdbc.JdbcUserRepository
import com.sohosai.sos.presenter.controller.GraphQLController
import com.sohosai.sos.presenter.resolver.UserResolver
import com.sohosai.sos.service.GraphQLService
import com.sohosai.sos.service.UserService
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.koin.dsl.module
import javax.sql.DataSource

object KoinModules {
    fun dev() = database() + repositories() + services() + resolvers() + graphql()

    private fun resolvers() = module {
        single<List<GraphQLResolver<*>>> {
            listOf(
                UserResolver(get())
            )
        }
    }

    private fun services() = module {
        single { UserService(get()) }
    }

    private fun repositories() = module {
        single<UserRepository> { JdbcUserRepository(get()) }
    }

    private fun graphql() = module {
        single { GraphQLConfigurer.configure(get()) }
        single { GraphQLService(get()) }
        single { GraphQLController(get()) }
    }

    private fun database() = module {
        single(createdAtStart = true) {
            initDatabase(
                host = env.config.property("database.host").getString(),
                port = env.config.property("database.port").getString().toInt(),
                database = env.config.property("database.database").getString(),
                username = env.config.property("database.username").getString(),
                password = env.config.property("database.password").getString()
            )
        }
    }

    // TODO: Move to right place
    private fun initDatabase(host: String, port: Int, database: String, username: String, password: String): DataSource {
        val config = HikariConfig().apply {
            this.jdbcUrl = "jdbc:postgresql://${host}:${port}/${database}"
            this.username = username
            this.password = password
        }

        return HikariDataSource(config)
    }
}