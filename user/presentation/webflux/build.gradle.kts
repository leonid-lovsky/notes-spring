plugins {
    id("com.example.spring-boot-webflux")
    id("com.example.spring-boot-validation")
}

dependencies {
    implementation(projects.user.contractReactive)
    implementation(projects.user.domain)
}
