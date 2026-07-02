plugins {
    id("com.example.spring-boot-data-mongodb-reactive")
}

dependencies {
    implementation(projects.user.dataContractReactive)
}
