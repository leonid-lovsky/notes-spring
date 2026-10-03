plugins {
    id("com.example.service-synchronous")
}

dependencies {
    implementation(project(":note:service:service-commons"))
    implementation(project(":note:contract:contract-synchronous"))
}
