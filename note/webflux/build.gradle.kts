plugins {
    id("com.example.spring-boot-webflux")
}

dependencies {
    implementation(projects.note.dataContractReactive)
    implementation(projects.note.domain)
}
