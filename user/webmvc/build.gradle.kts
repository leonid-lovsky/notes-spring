plugins {
    id("com.example.spring-boot-webmvc")
}

dependencies {
    implementation(projects.user.dataContract)
    implementation(projects.user.domain)
}
