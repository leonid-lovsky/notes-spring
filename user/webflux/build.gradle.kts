plugins {
    id("com.example.spring-boot-webflux")
}

dependencies {
    implementation(projects.user.dataContractReactive)
}
