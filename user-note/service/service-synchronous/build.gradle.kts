plugins {
    id("com.example.commons-synchronous")
}

dependencies {
    implementation(project(":user-note:service:service-commons"))
    implementation(project(":user-note:contract:contract-commons"))
    implementation(project(":user-note:contract:contract-synchronous"))
}
