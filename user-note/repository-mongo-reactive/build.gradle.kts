plugins {
    id("com.example.service-reactive")

    id("com.example.spring-boot-data-mongodb-reactive")

    id("com.example.spring-boot-testcontainers")
    id("com.example.spring-boot-testcontainers-mongodb")
}

dependencies {
    api(project(":user-note:contract-reactive"))
}
