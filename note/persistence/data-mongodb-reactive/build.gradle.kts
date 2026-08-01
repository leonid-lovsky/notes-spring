plugins {
    id("com.example.spring-boot-data-mongodb-reactive")
}

dependencies {
    implementation(projects.note.contractReactive)
    implementation(projects.note.domain)
}
