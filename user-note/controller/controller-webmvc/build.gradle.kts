plugins {
    id("com.example.spring-boot-webmvc")
}

dependencies {
    implementation(project(":user-note:contract:contract-commons"))
    implementation(project(":user-note:contract:contract-synchronous"))
}
