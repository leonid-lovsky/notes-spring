plugins {
    id("com.example.spring-boot-application")

    id("com.example.spring-boot-database-mysql")

    id("com.example.spring-boot-testcontainers")
    id("com.example.spring-boot-testcontainers-mysql")
}

dependencies {
    implementation(project(":user-note:application-core"))
    implementation(project(":user-note:data-jdbc"))
}
