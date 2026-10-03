plugins {
    id("com.example.project-reactor")

    id("com.example.spring-boot-data-commons")
}

dependencies {
    implementation(project(":user-note:contract:contract-common"))
    implementation(project(":user-note:contract:contract-reactive"))
}
