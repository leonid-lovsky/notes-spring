plugins {
    id("com.example.spring-boot-data-mongodb")
}

dependencies {
    implementation(projects.note.contract)
    implementation(projects.note.domain)
}
