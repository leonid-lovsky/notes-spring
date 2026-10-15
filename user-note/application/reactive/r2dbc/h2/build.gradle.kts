plugins {
    id("com.example.spring-boot")
    id("com.example.spring-boot-bootable")
    id("com.example.spring-boot-actuator")
    id("com.example.spring-boot-webflux")

    id("com.example.spring-boot-data-r2dbc")
    id("com.example.spring-boot-database-r2dbc-h2")
}

dependencies {
    implementation(project(":user-note:service:reactive"))
    implementation(project(":user-note:data:reactive:r2dbc"))
    implementation(project(":user-note:controller:reactive"))

    testImplementation(project(":user-note:contract:reactive"))
}
