val koinVersion = "2.0.1"
dependencies {
    implementation(project(":domain"))
    implementation(project(":service"))

    implementation("com.graphql-java-kickstart:graphql-java-tools:5.7.1")
    // TODO: remove koin from dependencies
    implementation("org.koin:koin-core:$koinVersion")
}