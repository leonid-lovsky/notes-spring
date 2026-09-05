plugins {
    id("com.example.spring-boot-application")

    id("com.example.spring-boot-testcontainers")
    id("com.example.spring-boot-testcontainers-mongodb")
}

dependencies {
    implementation(project(":user-note:data-mongodb-reactive"))
}
