plugins {
    id("com.example.spring-boot-webmvc")
}

dependencies {
    implementation(project(":note:contract:contract-common"))
    implementation(project(":note:contract:contract-synchronous"))
}
