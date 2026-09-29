plugins {
    id("com.example.spring-boot-data-r2dbc")

    id("com.example.spring-boot-test-r2dbc-h2")
}

dependencies {
    implementation(project(":note:contract-common"))
    implementation(project(":note:contract-reactive"))
}
