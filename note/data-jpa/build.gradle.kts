plugins {
    id("com.example.spring-boot-data-jpa")
}

dependencies {
    implementation(projects.note.dataContract)
    implementation(projects.note.domain)
}
