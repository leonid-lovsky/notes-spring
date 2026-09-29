plugins {
    id("com.example.spring-boot-data-mongodb")
}

dependencies {
    implementation(project(":note:contract-common"))
    implementation(project(":note:contract-synchronous"))
}
