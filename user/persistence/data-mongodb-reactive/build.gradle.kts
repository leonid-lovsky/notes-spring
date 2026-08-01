plugins {
    id("com.example.spring-boot-data-mongodb-reactive")
}

dependencies {
    implementation(projects.user.contractReactive)
    implementation(projects.user.domain)
}
