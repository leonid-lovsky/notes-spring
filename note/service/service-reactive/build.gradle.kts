plugins {
    id("com.example.service-reactive")
}

dependencies {
    implementation(project(":note:service:service-commons"))
    implementation(project(":note:contract:contract-reactive"))
}
