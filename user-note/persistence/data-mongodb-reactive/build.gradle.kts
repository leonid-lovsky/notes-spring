plugins {
    id("com.example.spring-boot-data-mongodb-reactive")
}

dependencies {
    implementation(projects.userNote.contractReactive)
    implementation(projects.userNote.domain)
}
