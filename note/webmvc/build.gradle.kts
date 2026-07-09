plugins {
    id("com.example.spring-boot-webmvc")
}

dependencies {
    implementation(projects.note.dataContract)
    implementation(projects.note.domain)
}
