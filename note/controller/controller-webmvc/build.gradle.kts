plugins {
    id("com.example.spring-boot-webmvc")
}

dependencies {
    implementation(project(":note:contract:contract-commons"))
    implementation(project(":note:contract:contract-synchronous"))
}
