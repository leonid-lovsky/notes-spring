plugins {
    id("com.example.service-synchronous")
}

dependencies {
    implementation(project(":note:contract:synchronous"))
    implementation(project(":note:service:commons"))
}
