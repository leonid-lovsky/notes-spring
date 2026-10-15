plugins {
    id("com.example.spring-boot")
    id("com.example.spring-boot-bootable")
    id("com.example.spring-boot-actuator")
    id("com.example.spring-boot-webflux")

    id("com.example.spring-boot-data-r2dbc")
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
