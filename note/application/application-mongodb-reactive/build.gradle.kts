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
    implementation(project(":note:controller:controller-webflux"))
    implementation(project(":note:data:data-mongodb-reactive"))
}
