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
    implementation(project(":user-note:service:sync"))
    implementation(project(":user-note:data:mongo"))
    implementation(project(":user-note:controller:webmvc"))

    testImplementation(project(":user-note:contract:sync"))
}
