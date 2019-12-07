plugins {
    application
    kotlin("jvm") version "1.3.61"
}

group = "com.sohosai"
version = "1.0-SNAPSHOT"

val ktorVersion = "1.2.6"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
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
