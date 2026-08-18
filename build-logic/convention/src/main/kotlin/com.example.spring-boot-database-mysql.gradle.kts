plugins {
    id("com.example.spring-boot")
}

dependencies {
    runtimeOnly("com.mysql:mysql-connector-j")
    testRuntimeOnly("com.h2database:h2")
}
