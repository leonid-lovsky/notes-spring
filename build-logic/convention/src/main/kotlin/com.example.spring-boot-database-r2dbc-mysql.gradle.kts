plugins {
    id("com.example.spring-boot")
}

dependencies {
    runtimeOnly("io.asyncer:r2dbc-mysql")
    testRuntimeOnly("io.r2dbc:r2dbc-h2")
}
