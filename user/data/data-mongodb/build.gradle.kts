plugins {
    id("com.example.spring-boot-data-mongodb")
}

dependencies {
    implementation(project(":user:contract:contract-common"))
    implementation(project(":user:contract:contract-synchronous"))
}
