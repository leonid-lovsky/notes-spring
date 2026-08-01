plugins {
    id("com.example.spring-boot-webmvc")
    id("com.example.spring-boot-validation")
}

dependencies {
    implementation(projects.userNote.contract)
    implementation(projects.userNote.domain)
}
