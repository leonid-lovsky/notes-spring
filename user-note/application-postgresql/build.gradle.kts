plugins {
    id("com.example.spring-boot")
    id("com.example.spring-boot-bootable")
    id("com.example.spring-boot-actuator")
    id("com.example.spring-boot-webmvc")

    id("com.example.spring-boot-data-jdbc")
    id("com.example.spring-boot-database-postgresql")

    id("com.example.spring-boot-testcontainers")
    id("com.example.spring-boot-testcontainers-postgresql")
}

dependencies {
    implementation(project(":user-note:data-jdbc"))
}
