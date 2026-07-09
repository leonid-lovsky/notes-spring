plugins {
    id("com.example.spring-boot-webflux")
}

dependencies {
    implementation(projects.userNote.dataContractReactive)
    implementation(projects.userNote.domain)
}
