plugins {
    id("com.example.spring-boot")
}

dependencies {
    testImplementation("org.testcontainers:testcontainers-mysql")
    testRuntimeOnly("com.mysql:mysql-connector-j")
}
