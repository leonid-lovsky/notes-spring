plugins {
    id("com.example.spring-boot-webmvc")
}

dependencies {
    implementation(projects.note.contract)
    implementation(projects.note.domain)
}
