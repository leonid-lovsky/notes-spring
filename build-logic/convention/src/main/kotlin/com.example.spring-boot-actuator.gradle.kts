plugins {
    id("com.example.spring-boot")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    testImplementation("org.springframework.boot:spring-boot-starter-actuator-test")
}
