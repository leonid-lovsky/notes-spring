plugins {
    id("com.example.spring-boot-application")
    id("com.example.spring-boot-database-h2")
}

dependencies {
    implementation(projects.userNote.domain)
    implementation(projects.userNote.contract)
    implementation(projects.userNote.webmvc)
    implementation(projects.userNote.dataJpa)
}
