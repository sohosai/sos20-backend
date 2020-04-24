plugins {
    kotlin("jvm") version "1.3.71"
}

val koinVersion = "2.0.1"

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenCentral()
        jcenter()
    }

    dependencies {
        implementation(kotlin("stdlib-jdk8"))
        implementation( "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")
        implementation("com.squareup.moshi:moshi:1.9.2")
        implementation("com.squareup.moshi:moshi-kotlin:1.9.2")
        implementation("org.koin:koin-core:$koinVersion")
        implementation("org.koin:koin-core-ext:$koinVersion")
        implementation("ch.qos.logback:logback-classic:1.2.3")
        implementation("io.sentry:sentry-logback:1.7.30")
    }

    group = "com.sohosai"
    version = "1.0-SNAPSHOT"

    tasks {
        compileKotlin {
            kotlinOptions.jvmTarget = "1.8"
            kotlinOptions.freeCompilerArgs = listOf(
                "-Xjsr305=strict",
                "-Xinline-classes",
                "-Xuse-experimental=io.ktor.util.KtorExperimentalAPI",
                "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi"
            )
        }
        compileTestKotlin {
            kotlinOptions.jvmTarget = "1.8"
            kotlinOptions.freeCompilerArgs = listOf(
                "-Xjsr305=strict",
                "-Xinline-classes",
                "-Xuse-experimental=io.ktor.util.KtorExperimentalAPI",
                "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi"
            )
        }
    }
}