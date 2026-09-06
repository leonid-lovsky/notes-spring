plugins {
    id("com.example.spring-boot")
    id("com.example.spring-boot-actuator")
    id("com.example.spring-boot-packaging")

    id("com.example.spring-boot-database-postgresql")

    id("com.example.spring-boot-testcontainers")
    id("com.example.spring-boot-testcontainers-postgresql")
}

dependencies {
    implementation(project(":user-note:application-core"))
    implementation(project(":user-note:data-jdbc"))
}
