plugins {
    id("com.example.spring-boot")
    id("com.example.spring-boot-actuator")
    id("com.example.spring-boot-packaging")

    id("com.example.spring-boot-database-r2dbc-mysql")

    id("com.example.spring-boot-testcontainers")
    id("com.example.spring-boot-testcontainers-r2dbc")
    id("com.example.spring-boot-testcontainers-mysql")
}

dependencies {
    implementation(project(":user-note:application-core"))
    implementation(project(":user-note:data-r2dbc"))
}
