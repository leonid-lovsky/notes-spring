plugins {
    id("com.example.spring-boot-data-jdbc")
}

dependencies {
    implementation(projects.userNote.dataContract)
    implementation(projects.userNote.domain)
}
