plugins {
    id("com.example.spring-boot-application")
    id("com.example.spring-boot-testcontainers-mongodb")
    id("com.example.spring-boot-docker-compose")
}

dependencies {
    implementation(projects.note.domain)
    implementation(projects.note.contractReactive)
    implementation(projects.note.webflux)
    implementation(projects.note.dataMongodbReactive)
}
