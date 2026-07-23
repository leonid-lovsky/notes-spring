plugins {
    id("com.example.spring-boot-application")
    id("com.example.spring-boot-h2-database")
    id("com.example.spring-boot-postgresql-database")
    id("com.example.spring-boot-mysql-database")
    id("com.example.spring-boot-testcontainers")
    id("com.example.spring-boot-testcontainers-postgresql")
    id("com.example.spring-boot-testcontainers-mysql")
    id("com.example.spring-boot-docker-compose")
}

dependencies {
    implementation(projects.note.domain)
    implementation(projects.note.contract)
    implementation(projects.note.webmvc)
    implementation(projects.note.dataJdbc)
}
