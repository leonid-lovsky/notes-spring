plugins {
    id("com.example.spring-boot-bootable")
    id("com.example.spring-boot-webflux")

    id("com.example.spring-boot-data-r2dbc")

    id("com.example.spring-boot-database-r2dbc-h2")
    id("com.example.spring-boot-database-r2dbc-mysql")
    id("com.example.spring-boot-database-r2dbc-postgresql")

    id("com.example.spring-boot-testcontainers")
    id("com.example.spring-boot-testcontainers-r2dbc")
    id("com.example.spring-boot-testcontainers-mysql")
    id("com.example.spring-boot-testcontainers-postgresql")
}

dependencies {
    implementation(project(":user-note:service:reactive"))
    implementation(project(":user-note:repository:r2dbc"))
    implementation(project(":user-note:controller:webflux"))
}

val testSourceSet = the<SourceSetContainer>()["test"]

tasks.named<Test>("test") {
    systemProperty("spring.profiles.active", "h2")
}

val vendorTests = listOf("mysql", "postgresql").map { vendor ->
    tasks.register<Test>("test${vendor.replaceFirstChar(Char::uppercase)}") {
        group = "verification"
        description = "Runs the tests with the $vendor profile."
        testClassesDirs = testSourceSet.output.classesDirs
        classpath = testSourceSet.runtimeClasspath
        systemProperty("spring.profiles.active", vendor)
        useJUnitPlatform()
    }
}

tasks.named("check") {
    dependsOn(vendorTests)
}
