plugins {
    id("com.example.spring-boot-data-jdbc")

    id("com.example.spring-boot-test-h2")
}

dependencies {
    implementation(project(":user-note:contract:contract-common"))
    implementation(project(":user-note:contract:contract-synchronous"))
}
