plugins {
    id("com.example.spring-boot-webmvc")
}

dependencies {
    implementation(project(":note:contract-common"))
    implementation(project(":note:contract-synchronous"))
}
