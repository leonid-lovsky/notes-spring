plugins {
    id("com.example.service-synchronous")
}

dependencies {
    implementation(project(":user:service:service-commons"))
    implementation(project(":user:contract:contract-synchronous"))
}
