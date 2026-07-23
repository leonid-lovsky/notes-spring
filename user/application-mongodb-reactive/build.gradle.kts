plugins {
    id("com.example.spring-boot-application")
}

dependencies {
    implementation(projects.user.domain)
    implementation(projects.user.contractReactive)
    implementation(projects.user.webflux)
    implementation(projects.user.dataMongodbReactive)
}
