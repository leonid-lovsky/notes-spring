plugins {
    id("com.example.spring-boot")
    id("com.example.spring-boot-bootable")
    id("com.example.spring-boot-actuator")
    id("com.example.spring-boot-webflux")

    id("com.example.spring-boot-data-mongodb-reactive")

    id("com.example.spring-boot-testcontainers")
    id("com.example.spring-boot-testcontainers-mongodb")
}

dependencies {
    implementation(project(":user-note:controller-webflux"))
    implementation(project(":user-note:data-mongodb-reactive"))
}
