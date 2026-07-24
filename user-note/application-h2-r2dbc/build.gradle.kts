plugins {
    id("com.example.spring-boot-application")
    id("com.example.spring-boot-database-h2-r2dbc")
}

dependencies {
    implementation(projects.userNote.domain)
    implementation(projects.userNote.contractReactive)
    implementation(projects.userNote.webflux)
    implementation(projects.userNote.dataR2dbc)
}
