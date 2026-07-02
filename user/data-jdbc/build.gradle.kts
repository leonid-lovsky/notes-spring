plugins {
    id("com.example.spring-boot-data-jdbc")
}

dependencies {
    implementation(projects.user.dataContract)
}
