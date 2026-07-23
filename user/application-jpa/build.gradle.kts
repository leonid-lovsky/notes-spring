plugins {
    id("com.example.spring-boot-application")
    id("com.example.spring-boot-h2-database")
    id("com.example.spring-boot-postgresql-database")
    id("com.example.spring-boot-mysql-database")
}

dependencies {
    implementation(projects.user.domain)
    implementation(projects.user.contract)
    implementation(projects.user.webmvc)
    implementation(projects.user.dataJpa)
}
