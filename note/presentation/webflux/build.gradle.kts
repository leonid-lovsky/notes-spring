plugins {
    id("com.example.spring-boot-webflux")
    id("com.example.spring-boot-validation")
}

dependencies {
    implementation(projects.note.contractReactive)
    implementation(projects.note.domain)
}
