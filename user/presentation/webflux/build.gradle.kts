plugins {
    id("com.example.spring-boot-webflux")
}

dependencies {
    implementation(projects.user.contractReactive)
    implementation(projects.user.domain)
}
