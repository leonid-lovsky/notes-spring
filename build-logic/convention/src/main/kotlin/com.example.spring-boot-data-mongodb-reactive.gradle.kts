plugins {
    id("com.example.spring-boot")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    testImplementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive-test")
}
