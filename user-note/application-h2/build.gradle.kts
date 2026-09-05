plugins {
    id("com.example.spring-boot-application")

    id("com.example.spring-boot-database-h2")
}

dependencies {
    implementation(project(":user-note:data-jdbc"))
    implementation(project(":user-note:data-jpa"))
}
