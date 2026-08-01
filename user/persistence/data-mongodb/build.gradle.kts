plugins {
    id("com.example.spring-boot-data-mongodb")
}

dependencies {
    implementation(projects.user.contract)
    implementation(projects.user.domain)
}
