plugins {
    id("com.example.service-reactive")
}

dependencies {
    implementation(project(":note:contract:contract-reactive"))
    implementation(project(":note:service:service-commons"))
}
