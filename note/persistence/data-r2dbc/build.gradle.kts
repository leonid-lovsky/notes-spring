plugins {
    id("com.example.spring-boot-data-r2dbc")
}

dependencies {
    implementation(projects.note.contractReactive)
    implementation(projects.note.domain)
}
