plugins {
    id("com.example.spring-boot-data-mongodb-reactive")
}

dependencies {
    implementation(project(":user:contract-common"))
    implementation(project(":user:contract-reactive"))
}
