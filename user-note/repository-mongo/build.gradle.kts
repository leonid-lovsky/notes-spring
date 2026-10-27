plugins {
    id("com.example.service-synchronous")

    id("com.example.spring-boot-data-mongodb")

    id("com.example.spring-boot-testcontainers")
    id("com.example.spring-boot-testcontainers-mongodb")
}

dependencies {
    api(project(":user-note:contract-imperative"))
}
