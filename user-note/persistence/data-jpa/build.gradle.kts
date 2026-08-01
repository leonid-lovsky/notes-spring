plugins {
    id("com.example.spring-boot-data-jpa")
}

dependencies {
    implementation(projects.userNote.contract)
    implementation(projects.userNote.domain)
}
