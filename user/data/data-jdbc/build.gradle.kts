plugins {
    id("com.example.spring-boot-data-jdbc")

    id("com.example.spring-boot-test-h2")
}

dependencies {
    implementation(project(":user:contract:contract-common"))
    implementation(project(":user:contract:contract-synchronous"))
}
