plugins {
    id("com.example.spring-boot-application")
    id("com.example.spring-boot-actuator")
    id("com.example.spring-boot-database-h2")
}

dependencies {
    implementation(projects.note.domain)
    implementation(projects.note.contract)
    implementation(projects.note.webmvc)
    implementation(projects.note.dataJpa)
}
