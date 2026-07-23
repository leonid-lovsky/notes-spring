plugins {
    id("com.example.spring-boot-application")
    id("com.example.spring-boot-r2dbc-h2-database")
    id("com.example.spring-boot-r2dbc-postgresql-database")
    id("com.example.spring-boot-r2dbc-mysql-database")
}

dependencies {
    implementation(projects.userNote.domain)
    implementation(projects.userNote.contractReactive)
    implementation(projects.userNote.webflux)
    implementation(projects.userNote.dataR2dbc)
}
