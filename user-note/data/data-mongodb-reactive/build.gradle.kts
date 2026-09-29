plugins {
    id("com.example.spring-boot-data-mongodb-reactive")
}

dependencies {
    implementation(project(":user-note:contract:contract-common"))
    implementation(project(":user-note:contract:contract-reactive"))
}
