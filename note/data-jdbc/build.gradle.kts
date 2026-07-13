plugins {
    id("com.example.spring-boot-data-jdbc")
}

dependencies {
    implementation(projects.note.contract)
    implementation(projects.note.domain)
}
