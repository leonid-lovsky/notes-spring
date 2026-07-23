plugins {
    id("com.example.spring-boot-application")
    id("com.example.spring-boot-database-r2dbc-h2")
    id("com.example.spring-boot-database-r2dbc-postgresql")
    id("com.example.spring-boot-database-r2dbc-mysql")
    id("com.example.spring-boot-testcontainers")
    id("com.example.spring-boot-testcontainers-r2dbc")
    id("com.example.spring-boot-testcontainers-postgresql")
    id("com.example.spring-boot-testcontainers-mysql")
    id("com.example.spring-boot-docker-compose")
}

dependencies {
    implementation(projects.user.domain)
    implementation(projects.user.contractReactive)
    implementation(projects.user.webflux)
    implementation(projects.user.dataR2dbc)
}
