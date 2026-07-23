plugins {
    id("com.example.spring-boot-application")
    id("com.example.spring-boot-testcontainers-mongodb")
    id("com.example.spring-boot-docker-compose")
}

dependencies {
    implementation(projects.user.domain)
    implementation(projects.user.contract)
    implementation(projects.user.webmvc)
    implementation(projects.user.dataMongodb)
}
