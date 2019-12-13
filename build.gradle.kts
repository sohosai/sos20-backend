plugins {
    application
    kotlin("jvm") version "1.3.61"
}

group = "com.sohosai"
version = "1.0-SNAPSHOT"

val ktorVersion = "1.2.6"
val koinVersion = "2.0.1"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-gson:$ktorVersion")
    implementation("com.graphql-java-kickstart:graphql-java-tools:5.7.1")

    implementation("org.koin:koin-core:$koinVersion")
    implementation("org.koin:koin-core-ext:$koinVersion")
    implementation("org.koin:koin-ktor:$koinVersion")

    implementation("com.zaxxer:HikariCP:3.4.1")
    implementation("org.flywaydb:flyway-core:6.1.1")
    implementation("org.postgresql:postgresql:42.2.9")

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
            "-Xuse-experimental=io.ktor.util.KtorExperimentalAPI"
        )
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions.freeCompilerArgs = listOf(
            "-Xjsr305=strict",
            "-Xuse-experimental=io.ktor.util.KtorExperimentalAPI"
        )
    }
}
