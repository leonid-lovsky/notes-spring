plugins {
    id("com.example.spring-boot-application")
    id("com.example.spring-boot-database-h2-r2dbc")
}

dependencies {
    implementation(projects.note.domain)
    implementation(projects.note.contractReactive)
    implementation(projects.note.webflux)
    implementation(projects.note.dataR2dbc)
}
