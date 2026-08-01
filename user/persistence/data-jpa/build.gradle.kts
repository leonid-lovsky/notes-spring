plugins {
    id("com.example.spring-boot-data-jpa")
}

dependencies {
    implementation(projects.user.contract)
    implementation(projects.user.domain)
}
