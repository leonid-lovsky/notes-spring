plugins {
    id("com.example.spring-boot-application")
    id("com.example.spring-boot-h2-database")
    id("com.example.spring-boot-postgresql-database")
    id("com.example.spring-boot-mysql-database")
}

dependencies {
    implementation(projects.note.domain)
    implementation(projects.note.contract)
    implementation(projects.note.webmvc)
    implementation(projects.note.dataJdbc)
}
