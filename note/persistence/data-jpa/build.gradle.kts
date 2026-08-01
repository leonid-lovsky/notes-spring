plugins {
    id("com.example.spring-boot-data-jpa")
    id("com.example.spring-boot-validation")
}

dependencies {
    implementation(projects.note.contract)
    implementation(projects.note.domain)
}
