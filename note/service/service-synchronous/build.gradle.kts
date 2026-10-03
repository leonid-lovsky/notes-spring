plugins {
    id("com.example.commons-synchronous")
}

dependencies {
    implementation(project(":note:service:service-commons"))
    implementation(project(":note:contract:contract-commons"))
    implementation(project(":note:contract:contract-synchronous"))
}
