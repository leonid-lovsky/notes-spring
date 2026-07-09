plugins {
    id("com.example.spring-boot-webmvc")
}

dependencies {
    implementation(projects.userNote.dataContract)
    implementation(projects.userNote.domain)
}
