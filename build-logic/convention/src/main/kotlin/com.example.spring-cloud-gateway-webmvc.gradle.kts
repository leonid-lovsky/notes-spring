plugins {
    id("com.example.spring-cloud")
    id("org.springframework.boot")
}

dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-gateway-server-webmvc")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
