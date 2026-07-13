plugins {
    id("com.example.spring-boot-application")
    id("com.example.spring-boot-h2-database")
}

dependencies {
    implementation(projects.note.domain)
    implementation(projects.note.contract)
    implementation(projects.note.webmvc)
    implementation(projects.note.dataJpa)
}
