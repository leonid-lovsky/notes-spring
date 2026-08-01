plugins {
    id("com.example.spring-boot-application")
    id("com.example.spring-boot-actuator")
    id("com.example.spring-boot-database-h2")
}

dependencies {
    implementation(projects.user.domain)
    implementation(projects.user.contract)
    implementation(projects.user.webmvc)
    implementation(projects.user.dataJpa)
}
