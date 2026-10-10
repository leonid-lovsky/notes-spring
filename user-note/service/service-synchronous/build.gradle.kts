plugins {
    id("com.example.service-synchronous")
    id("com.example.spring-boot")
}

dependencies {
    implementation(project(":user-note:contract:contract-synchronous"))
}
