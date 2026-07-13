plugins {
    id("com.example.spring-boot-data-jpa")
}

dependencies {
    implementation(projects.note.contract)
    implementation(projects.note.domain)
}
