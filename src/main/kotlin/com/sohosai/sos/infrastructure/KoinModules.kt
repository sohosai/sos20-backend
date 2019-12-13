package com.sohosai.sos.infrastructure

import com.sohosai.sos.infrastructure.graphql.GraphQLConfigurer
import com.sohosai.sos.presenter.controller.GraphQLController
import com.sohosai.sos.service.GraphQLService
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.koin.dsl.module
import org.postgresql.ds.PGSimpleDataSource
import javax.sql.DataSource

object KoinModules {
    fun base() = module {
        single(createdAtStart = true) {
            initDatabase(
                host = env.config.property("database.host").getString(),
                port = env.config.property("database.port").getString().toInt(),
                database = env.config.property("database.database").getString(),
                username = env.config.property("database.username").getString(),
                password = env.config.property("database.password").getString()
            )
        }

        single { GraphQLConfigurer.configure() }
        single { GraphQLService(get()) }
        single { GraphQLController(get()) }
    }

    private fun initDatabase(host: String, port: Int, database: String, username: String, password: String): DataSource {
        val config = HikariConfig().apply {
            this.jdbcUrl = "jdbc:postgresql://${host}:${port}/${database}"
            this.username = username
            this.password = password
            this.driverClassName = PGSimpleDataSource::class.qualifiedName
        }

        return HikariDataSource(config)
    }
}