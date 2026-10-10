plugins {
    id("com.example.spring-boot-data-jdbc")
}

dependencies {
    implementation(project(":user-note:contract:contract-synchronous"))
}
