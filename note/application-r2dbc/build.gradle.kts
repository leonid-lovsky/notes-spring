plugins {
    id("com.example.spring-boot-application")
    id("com.example.spring-boot-r2dbc-h2-database")
    id("com.example.spring-boot-r2dbc-postgresql-database")
    id("com.example.spring-boot-r2dbc-mysql-database")
    id("com.example.spring-boot-testcontainers")
    id("com.example.spring-boot-testcontainers-r2dbc")
    id("com.example.spring-boot-testcontainers-postgresql")
    id("com.example.spring-boot-testcontainers-mysql")
    id("com.example.spring-boot-docker-compose")
}

dependencies {
    implementation(projects.note.domain)
    implementation(projects.note.contractReactive)
    implementation(projects.note.webflux)
    implementation(projects.note.dataR2dbc)
}
