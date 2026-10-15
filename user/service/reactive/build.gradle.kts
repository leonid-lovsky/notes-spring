plugins {
    id("com.example.service-reactive")
}

dependencies {
    implementation(project(":user:contract:reactive"))
    implementation(project(":user:service:commons"))
}
