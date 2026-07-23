plugins {
    id("com.example.spring-boot-application")
}

dependencies {
    implementation(projects.note.domain)
    implementation(projects.note.contract)
    implementation(projects.note.webmvc)
    implementation(projects.note.dataMongodb)
}
