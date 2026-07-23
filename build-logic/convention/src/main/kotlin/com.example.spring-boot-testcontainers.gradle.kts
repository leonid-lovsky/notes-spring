plugins {
    id("com.example.spring-boot")
}

dependencies {
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:testcontainers-junit-jupiter")
}
