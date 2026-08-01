plugins {
    id("com.example.spring-boot-data-jpa")
    id("com.example.spring-boot-validation")
}

dependencies {
    implementation(projects.user.contract)
    implementation(projects.user.domain)
}
