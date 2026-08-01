plugins {
    id("com.example.spring-boot-data-r2dbc")
}

dependencies {
    implementation(projects.userNote.contractReactive)
    implementation(projects.userNote.domain)
}
