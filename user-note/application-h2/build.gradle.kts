plugins {
    id("com.example.spring-boot")
    id("com.example.spring-boot-actuator")
    id("com.example.spring-boot-packaging")

    id("com.example.spring-boot-database-h2")
}

dependencies {
    implementation(project(":user-note:application-core"))
    implementation(project(":user-note:data-jdbc"))
}
