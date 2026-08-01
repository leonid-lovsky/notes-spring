plugins {
    id("com.example.spring-boot-data-jdbc")
}

dependencies {
    implementation(projects.userNote.contract)
    implementation(projects.userNote.domain)
}
