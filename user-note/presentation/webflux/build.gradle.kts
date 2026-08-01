plugins {
    id("com.example.spring-boot-webflux")
    id("com.example.spring-boot-validation")
}

dependencies {
    implementation(projects.userNote.contractReactive)
    implementation(projects.userNote.domain)
}
