plugins {
    id("com.example.spring-boot-webmvc")
}

dependencies {
    implementation(projects.user.contract)
    implementation(projects.user.domain)
}
