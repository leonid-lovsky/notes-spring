plugins {
    id("com.example.service-synchronous")
}

dependencies {
    implementation(project(":user:contract:contract-synchronous"))
    implementation(project(":user:service:service-commons"))
}
