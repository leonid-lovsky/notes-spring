plugins {
    id("com.example.spring-boot-data-jdbc")
}

dependencies {
    implementation(projects.note.dataContract)
    implementation(projects.note.domain)
}
