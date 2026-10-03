plugins {
    id("com.example.spring-boot")
    id("com.example.spring-boot-bootable")
    id("com.example.spring-boot-actuator")
    id("com.example.spring-boot-webflux")

    id("com.example.spring-boot-data-r2dbc")
    id("com.example.spring-boot-database-r2dbc-mysql")

    id("com.example.spring-boot-testcontainers")
    id("com.example.spring-boot-testcontainers-r2dbc")
    id("com.example.spring-boot-testcontainers-mysql")
}

dependencies {
    implementation(project(":note:service:service-reactive"))
    implementation(project(":note:controller:controller-webflux"))
}
