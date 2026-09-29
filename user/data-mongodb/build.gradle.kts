plugins {
    id("com.example.spring-boot-data-mongodb")
}

dependencies {
    implementation(project(":user:contract-common"))
    implementation(project(":user:contract-synchronous"))
}
