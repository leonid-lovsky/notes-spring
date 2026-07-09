plugins {
    id("com.example.spring-boot-data-mongodb")
}

dependencies {
    implementation(projects.note.dataContract)
    implementation(projects.note.domain)
}
