plugins {
    id("com.example.spring-boot-data-jpa")
    id("com.example.spring-boot-validation")
}

dependencies {
    implementation(projects.userNote.contract)
    implementation(projects.userNote.domain)
}
