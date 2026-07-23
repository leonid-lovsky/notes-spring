plugins {
    id("com.example.spring-boot-application")
}

dependencies {
    implementation(projects.note.domain)
    implementation(projects.note.contractReactive)
    implementation(projects.note.webflux)
    implementation(projects.note.dataMongodbReactive)
}
