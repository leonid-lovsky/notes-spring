plugins {
    id("com.example.spring-boot-application")
    id("com.example.spring-boot-h2-database")
}

dependencies {
    implementation(projects.user.domain)
    implementation(projects.user.dataContract)
    implementation(projects.user.webmvc)
    implementation(projects.user.dataJpa)
}
