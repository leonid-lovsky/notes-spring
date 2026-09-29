plugins {
    id("com.example.spring-boot-data-mongodb-reactive")
}

dependencies {
    implementation(project(":note:contract:contract-common"))
    implementation(project(":note:contract:contract-reactive"))
}
