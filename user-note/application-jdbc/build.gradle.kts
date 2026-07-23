plugins {
    id("com.example.spring-boot-application")
    id("com.example.spring-boot-h2-database")
    id("com.example.spring-boot-postgresql-database")
    id("com.example.spring-boot-mysql-database")
}

dependencies {
    implementation(projects.userNote.domain)
    implementation(projects.userNote.contract)
    implementation(projects.userNote.webmvc)
    implementation(projects.userNote.dataJdbc)
}
