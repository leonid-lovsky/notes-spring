plugins {
    id("com.example.commons-reactive")
}

dependencies {
    implementation(project(":user:service:service-commons"))
    implementation(project(":user:contract:contract-commons"))
    implementation(project(":user:contract:contract-reactive"))
}
