plugins {
    id("com.example.spring-boot")
    id("com.example.reactor")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webclient")
    testImplementation("org.springframework.boot:spring-boot-starter-webclient-test")
}
