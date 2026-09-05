plugins {
    id("com.example.spring-boot-application")

    id("com.example.spring-boot-database-r2dbc-h2")
}

dependencies {
    implementation(project(":user-note:data-r2dbc"))
}
