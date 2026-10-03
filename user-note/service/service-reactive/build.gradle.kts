plugins {
    id("com.example.service-reactive")
}

dependencies {
    implementation(project(":user-note:contract:contract-reactive"))
    implementation(project(":user-note:service:service-commons"))
}
