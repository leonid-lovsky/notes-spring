plugins {
    id("com.example.spring-boot")
}

dependencies {
    runtimeOnly("org.postgresql:postgresql")
    testRuntimeOnly("com.h2database:h2")
}
