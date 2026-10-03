plugins {
    id("com.example.spring-boot-data-commons")
}

dependencies {
    implementation(project(":user-note:contract:contract-common"))
    implementation(project(":user-note:contract:contract-synchronous"))
}
