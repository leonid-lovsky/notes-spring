plugins {
    id("com.example.service-reactive")
}

dependencies {
    implementation(project(":user:contract:contract-reactive"))
    implementation(project(":user:service:service-commons"))
}
