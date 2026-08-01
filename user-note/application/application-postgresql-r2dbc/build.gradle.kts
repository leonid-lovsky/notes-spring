plugins {
    id("com.example.spring-boot-application")
    id("com.example.spring-boot-actuator")
    id("com.example.spring-boot-database-postgresql-r2dbc")
    id("com.example.spring-boot-testcontainers")
    id("com.example.spring-boot-testcontainers-r2dbc")
    id("com.example.spring-boot-testcontainers-postgresql")
    id("com.example.spring-boot-docker-compose")
}

dependencies {
    implementation(projects.userNote.domain)
    implementation(projects.userNote.contractReactive)
    implementation(projects.userNote.webflux)
    implementation(projects.userNote.dataR2dbc)
}
