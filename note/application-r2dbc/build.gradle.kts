plugins {
    id("com.example.spring-boot-application")
    id("com.example.spring-boot-r2dbc-h2-database")
    id("com.example.spring-boot-r2dbc-postgresql-database")
    id("com.example.spring-boot-r2dbc-mysql-database")
}

dependencies {
    implementation(projects.note.domain)
    implementation(projects.note.contractReactive)
    implementation(projects.note.webflux)
    implementation(projects.note.dataR2dbc)
}
