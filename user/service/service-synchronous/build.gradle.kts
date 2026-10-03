plugins {
    id("com.example.spring-boot-data-commons")
}

dependencies {
    implementation(project(":user:contract:contract-common"))
    implementation(project(":user:contract:contract-synchronous"))
}
