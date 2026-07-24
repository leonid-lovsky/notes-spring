plugins {
    id("com.example.spring-boot-application")
    id("com.example.spring-boot-database-h2-r2dbc")
}

dependencies {
    implementation(projects.user.domain)
    implementation(projects.user.contractReactive)
    implementation(projects.user.webflux)
    implementation(projects.user.dataR2dbc)
}
