plugins {
    id("com.example.spring-boot")
    id("com.example.spring-boot-bootable")
    id("com.example.spring-boot-actuator")
    id("com.example.spring-boot-webflux")

    id("com.example.spring-boot-data-r2dbc")
    id("com.example.spring-boot-database-r2dbc-h2")
}

dependencies {
    implementation(project(":user:controller:controller-webflux"))
    implementation(project(":user:data:data-r2dbc"))
}
