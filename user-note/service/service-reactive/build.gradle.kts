plugins {
    id("com.example.service-reactive")
    id("com.example.spring-boot")
}

dependencies {
    implementation(project(":user-note:contract:contract-reactive"))
}
