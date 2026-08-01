plugins {
    id("com.example.spring-boot-webmvc")
    id("com.example.spring-boot-validation")
}

dependencies {
    implementation(projects.user.contract)
    implementation(projects.user.domain)
}
