plugins {
    id("com.example.service-synchronous")
}

dependencies {
    implementation(project(":user:contract:synchronous"))
    implementation(project(":user:service:commons"))
}
