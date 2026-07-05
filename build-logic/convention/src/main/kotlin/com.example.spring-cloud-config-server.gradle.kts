plugins {
    id("com.example.spring-cloud")
    id("com.example.spring-boot-application")
}

dependencies {
    implementation("org.springframework.cloud:spring-cloud-config-server")
}
