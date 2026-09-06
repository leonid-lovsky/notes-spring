plugins {
    id("com.example.spring-boot")
    id("com.example.spring-boot-actuator")
    id("com.example.spring-boot-packaging")

    id("com.example.spring-boot-database-r2dbc-postgresql")

    id("com.example.spring-boot-testcontainers")
    id("com.example.spring-boot-testcontainers-r2dbc")
    id("com.example.spring-boot-testcontainers-postgresql")
}

dependencies {
    implementation(project(":user-note:application-core"))
    implementation(project(":user-note:data-r2dbc"))
}
