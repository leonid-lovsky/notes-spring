plugins {
    id("com.example.spring-boot-application")
    id("com.example.spring-boot-actuator")
    id("com.example.spring-boot-testcontainers")
    id("com.example.spring-boot-testcontainers-mongodb")
    id("com.example.spring-boot-docker-compose")
}

dependencies {
    implementation(projects.user.domain)
    implementation(projects.user.contractReactive)
    implementation(projects.user.webflux)
    implementation(projects.user.dataMongodbReactive)
}
