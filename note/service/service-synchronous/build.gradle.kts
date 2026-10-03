plugins {
    id("com.example.service-synchronous")
}

dependencies {
    implementation(project(":note:contract:contract-synchronous"))
    implementation(project(":note:service:service-commons"))
}
