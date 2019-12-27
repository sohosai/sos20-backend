plugins {
    application
    kotlin("jvm") version "1.3.61"
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

group = "com.sohosai"
version = "1.0-SNAPSHOT"

val ktorVersion = "1.3.0-rc"
val koinVersion = "2.0.1"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-auth:$ktorVersion")
    implementation("io.ktor:ktor-auth-jwt:$ktorVersion")
    implementation("io.ktor:ktor-gson:$ktorVersion")
    implementation("com.graphql-java-kickstart:graphql-java-tools:5.7.1")

    implementation("org.koin:koin-core:$koinVersion")
    implementation("org.koin:koin-core-ext:$koinVersion")
    implementation("org.koin:koin-ktor:$koinVersion")

    implementation("org.postgresql:postgresql:42.2.9")
    implementation("com.zaxxer:HikariCP:3.4.1")
    implementation("org.flywaydb:flyway-core:6.1.1")
    implementation("com.github.seratch:kotliquery:1.3.1")

    implementation("com.auth0:jwks-rsa:0.9.0")

    implementation("ch.qos.logback:logback-classic:1.2.3")
}

application {
    this.mainClassName = "io.ktor.server.netty.EngineMain"
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions.freeCompilerArgs = listOf(
            "-Xjsr305=strict",
            "-Xinline-classes",
            "-Xuse-experimental=io.ktor.util.KtorExperimentalAPI"
        )
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions.freeCompilerArgs = listOf(
            "-Xjsr305=strict",
            "-Xinline-classes",
            "-Xuse-experimental=io.ktor.util.KtorExperimentalAPI"
        )
    }
}