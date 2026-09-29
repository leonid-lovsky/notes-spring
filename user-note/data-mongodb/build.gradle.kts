plugins {
    id("com.example.spring-boot-data-mongodb")
}

dependencies {
    implementation(project(":user-note:contract-common"))
    implementation(project(":user-note:contract-synchronous"))
}
