plugins {
    id("com.example.spring-boot")
    id("com.example.spring-boot-bootable")
    id("com.example.spring-boot-actuator")
    id("com.example.spring-boot-webflux")

    id("com.example.spring-boot-data-r2dbc")
    id("com.example.spring-boot-database-r2dbc-h2")
}

dependencies {
    implementation(project(":note:controller:controller-webflux"))
    implementation(project(":note:service:service-reactive"))
}
