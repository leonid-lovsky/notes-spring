plugins {
    id("com.example.service-synchronous")

    id("com.example.spring-boot-data-jdbc")

    id("com.example.spring-boot-database-h2")
    id("com.example.spring-boot-database-mysql")
    id("com.example.spring-boot-database-postgresql")

    id("com.example.spring-boot-testcontainers")
    id("com.example.spring-boot-testcontainers-mysql")
    id("com.example.spring-boot-testcontainers-postgresql")
}

dependencies {
    api(project(":user-note:contract:imperative"))
}

val testSourceSet = the<SourceSetContainer>()["test"]

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
