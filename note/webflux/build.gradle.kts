plugins {
    id("com.example.spring-boot-webflux")
}

dependencies {
    implementation(projects.note.contractReactive)
    implementation(projects.note.domain)
}
