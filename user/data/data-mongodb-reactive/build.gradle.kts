plugins {
    id("com.example.spring-boot-data-mongodb-reactive")
}

dependencies {
    implementation(project(":user:contract:contract-common"))
    implementation(project(":user:contract:contract-reactive"))
}
