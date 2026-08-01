plugins {
    id("com.example.spring-boot-webflux")
}

dependencies {
    implementation(projects.userNote.contractReactive)
    implementation(projects.userNote.domain)
}
