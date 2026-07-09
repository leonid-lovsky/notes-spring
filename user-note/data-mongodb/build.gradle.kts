plugins {
    id("com.example.spring-boot-data-mongodb")
}

dependencies {
    implementation(projects.userNote.dataContract)
    implementation(projects.userNote.domain)
}
