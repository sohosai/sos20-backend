package com.sohosai.sos.infrastructure

import com.coxautodev.graphql.tools.GraphQLResolver
import com.sohosai.sos.database.JdbcProjectRepository
import com.sohosai.sos.database.JdbcUserRepository
import com.sohosai.sos.domain.project.ProjectRepository
import com.sohosai.sos.infrastructure.graphql.GraphQLConfigurer
import com.sohosai.sos.infrastructure.graphql.GraphQLHandler
import com.sohosai.sos.interfaces.user.UserMutationResolver
import com.sohosai.sos.interfaces.user.UserQueryResolver
import com.sohosai.sos.domain.user.UserRepository
import com.sohosai.sos.interfaces.project.ProjectMutationResolver
import com.sohosai.sos.interfaces.project.ProjectResolver
import com.sohosai.sos.service.UserService
import com.sohosai.sos.service.ProjectService
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.koin.dsl.module
import org.koin.experimental.builder.single
import javax.sql.DataSource

object KoinModules {
    fun dev() = database() + repositories() + services() + resolvers() + graphql()

    private fun resolvers() = module {
        single<List<GraphQLResolver<*>>> {
            listOf(
                UserQueryResolver(get()),
                UserMutationResolver(get()),

                ProjectResolver(get()),
                ProjectMutationResolver(get())
            )
        }
    }

    private fun services() = module {
        single { UserService(get()) }
        single<ProjectService>()
    }

    private fun repositories() = module {
        single<UserRepository> { JdbcUserRepository(get()) }
        single<ProjectRepository> { JdbcProjectRepository(get()) }
    }

    private fun graphql() = module {
        single { GraphQLConfigurer.configure(get()) }
        single { GraphQLHandler(get()) }
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