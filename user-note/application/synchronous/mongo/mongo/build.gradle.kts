plugins {
    id("com.example.spring-boot")
    id("com.example.spring-boot-bootable")
    id("com.example.spring-boot-actuator")
    id("com.example.spring-boot-webmvc")

    id("com.example.spring-boot-data-mongodb")

    id("com.example.spring-boot-testcontainers")
    id("com.example.spring-boot-testcontainers-mongodb")
}

dependencies {
    implementation(project(":user-note:service:synchronous"))
    implementation(project(":user-note:data:synchronous:mongo"))
    implementation(project(":user-note:controller:synchronous"))

    testImplementation(project(":user-note:contract:synchronous"))
}
