plugins {
    id("com.example.spring-boot-webmvc")
}

dependencies {
    implementation(project(":user:contract:contract-commons"))
    implementation(project(":user:contract:contract-synchronous"))
}
