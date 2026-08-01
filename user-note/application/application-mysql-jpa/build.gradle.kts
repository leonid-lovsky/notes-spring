plugins {
    id("com.example.spring-boot-application")
    id("com.example.spring-boot-database-mysql")
    id("com.example.spring-boot-testcontainers")
    id("com.example.spring-boot-testcontainers-mysql")
    id("com.example.spring-boot-docker-compose")
}

dependencies {
    implementation(projects.userNote.domain)
    implementation(projects.userNote.contract)
    implementation(projects.userNote.webmvc)
    implementation(projects.userNote.dataJpa)
}
