plugins {
    id("com.example.spring-boot-application")
}

dependencies {
    implementation(projects.userNote.domain)
    implementation(projects.userNote.contractReactive)
    implementation(projects.userNote.webflux)
    implementation(projects.userNote.dataMongodbReactive)
}
