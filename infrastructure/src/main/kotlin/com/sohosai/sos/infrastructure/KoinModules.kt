package com.sohosai.sos.infrastructure

import com.sohosai.sos.database.JdbcApplicationRepository
import com.sohosai.sos.database.JdbcProjectRepository
import com.sohosai.sos.database.JdbcUserRepository
import com.sohosai.sos.database.LocalFileRepository
import com.sohosai.sos.domain.application.ApplicationRepository
import com.sohosai.sos.domain.file.FileRepository
import com.sohosai.sos.domain.project.ProjectRepository
import com.sohosai.sos.domain.user.UserRepository
import com.sohosai.sos.service.ApplicationService
import com.sohosai.sos.service.FileService
import com.sohosai.sos.service.ProjectService
import com.sohosai.sos.service.UserService
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.koin.dsl.module
import org.koin.experimental.builder.single
import javax.sql.DataSource

object KoinModules {
    fun dev() = database() + repositories() + services()

    private fun services() = module {
        single<UserService>()
        single<ProjectService>()
        single<ApplicationService>()
        single<FileService>()
    }

    private fun repositories() = module {
        single<UserRepository> { JdbcUserRepository(get()) }
        single<ProjectRepository> { JdbcProjectRepository(get()) }
        single<ApplicationRepository> { JdbcApplicationRepository(get()) }
        single<FileRepository> { LocalFileRepository() }
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