plugins {
    id("com.example.spring-boot-application")
    id("com.example.spring-boot-h2-database")
}

dependencies {
    implementation(projects.userNote.domain)
    implementation(projects.userNote.dataContract)
    implementation(projects.userNote.webmvc)
    implementation(projects.userNote.dataJpa)
}
