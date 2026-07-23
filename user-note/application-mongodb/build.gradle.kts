plugins {
    id("com.example.spring-boot-application")
}

dependencies {
    implementation(projects.userNote.domain)
    implementation(projects.userNote.contract)
    implementation(projects.userNote.webmvc)
    implementation(projects.userNote.dataMongodb)
}
