plugins {
    id("com.example.spring-boot")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-batch-jdbc")
    testImplementation("org.springframework.boot:spring-boot-starter-batch-jdbc-test")
}
