plugins {
    id("com.example.spring-boot-application")
    id("com.example.spring-boot-database-h2")
    id("com.example.spring-boot-database-postgresql")
    id("com.example.spring-boot-database-mysql")
    id("com.example.spring-boot-testcontainers")
    id("com.example.spring-boot-testcontainers-postgresql")
    id("com.example.spring-boot-testcontainers-mysql")
    id("com.example.spring-boot-docker-compose")
}

dependencies {
    implementation(projects.user.domain)
    implementation(projects.user.contract)
    implementation(projects.user.webmvc)
    implementation(projects.user.dataJdbc)
}
