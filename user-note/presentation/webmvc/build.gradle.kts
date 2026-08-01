plugins {
    id("com.example.spring-boot-webmvc")
}

dependencies {
    implementation(projects.userNote.contract)
    implementation(projects.userNote.domain)
}
