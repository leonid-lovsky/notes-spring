plugins {
    id("com.example.service-synchronous")
}

dependencies {
    implementation(project(":user-note:contract:contract-synchronous"))
    implementation(project(":user-note:service:service-commons"))
}
