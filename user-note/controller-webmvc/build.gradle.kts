plugins {
    id("com.example.spring-boot-webmvc")
}

dependencies {
    implementation(project(":user-note:contract-common"))
    implementation(project(":user-note:contract-synchronous"))
}
