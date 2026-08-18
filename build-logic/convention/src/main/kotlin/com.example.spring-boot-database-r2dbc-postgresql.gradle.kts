plugins {
    id("com.example.spring-boot")
}

dependencies {
    runtimeOnly("org.postgresql:r2dbc-postgresql")
    testRuntimeOnly("io.r2dbc:r2dbc-h2")
}
