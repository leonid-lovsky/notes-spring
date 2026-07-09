plugins {
    id("com.example.spring-boot-data-r2dbc")
}

dependencies {
    implementation(projects.userNote.dataContractReactive)
    implementation(projects.userNote.domain)
}
