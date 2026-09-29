plugins {
    id("com.example.spring-boot-data-mongodb")
}

dependencies {
    implementation(project(":note:contract:contract-common"))
    implementation(project(":note:contract:contract-synchronous"))
}
