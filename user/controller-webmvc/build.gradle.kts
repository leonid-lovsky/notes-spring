plugins {
    id("com.example.spring-boot-webmvc")
}

dependencies {
    implementation(project(":user:contract-common"))
    implementation(project(":user:contract-synchronous"))
}
