plugins {
    id("com.example.service-reactive")
}

dependencies {
    implementation(project(":note:contract:reactive"))
    implementation(project(":note:service:commons"))
}
