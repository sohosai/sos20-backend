plugins {
    application
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

val ktorVersion = "1.3.0-rc"
val koinVersion = "2.0.1"

dependencies {
    implementation(project(":domain"))
    implementation(project(":service"))
    implementation(project(":database"))
    implementation(project(":interfaces"))

    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-auth:$ktorVersion")
    implementation("io.ktor:ktor-auth-jwt:$ktorVersion")
    implementation("io.ktor:ktor-gson:$ktorVersion")

    implementation("org.koin:koin-core:$koinVersion")
    implementation("org.koin:koin-core-ext:$koinVersion")
    implementation("org.koin:koin-ktor:$koinVersion")

    implementation("com.zaxxer:HikariCP:3.4.1")
    implementation("org.flywaydb:flyway-core:6.1.1")
    implementation("org.postgresql:postgresql:42.2.9")

    implementation("com.auth0:jwks-rsa:0.9.0")
}

application {
    this.mainClassName = "io.ktor.server.netty.EngineMain"
}