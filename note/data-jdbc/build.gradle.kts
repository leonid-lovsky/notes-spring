plugins {
    id("com.example.spring-boot-data-jdbc")

    id("com.example.spring-boot-test-h2")
}

dependencies {
    implementation(project(":note:contract-common"))
    implementation(project(":note:contract-synchronous"))
}
