plugins {
    id("com.example.project-reactor")

    id("com.example.spring-boot-data-commons")
}

dependencies {
    implementation(project(":note:contract:contract-common"))
    implementation(project(":note:contract:contract-reactive"))
}
