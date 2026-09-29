plugins {
    id("com.example.spring-boot-data-mongodb-reactive")
}

dependencies {
    implementation(project(":note:contract-common"))
    implementation(project(":note:contract-reactive"))
}
