plugins {
    id("com.example.spring-boot")
    id("com.example.spring-boot-bootable")
    id("com.example.spring-boot-actuator")
    id("com.example.spring-boot-webmvc")

    id("com.example.spring-boot-data-jdbc")
    id("com.example.spring-boot-database-h2")
}

dependencies {
    implementation(project(":user-note:service:synchronous"))
    implementation(project(":user-note:data:synchronous:jdbc"))
    implementation(project(":user-note:controller:synchronous"))

    testImplementation(project(":user-note:contract:synchronous"))
}
