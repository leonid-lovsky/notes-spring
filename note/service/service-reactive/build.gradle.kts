plugins {
    id("com.example.commons-reactive")
}

dependencies {
    implementation(project(":note:service:service-commons"))
    implementation(project(":note:contract:contract-commons"))
    implementation(project(":note:contract:contract-reactive"))
}
