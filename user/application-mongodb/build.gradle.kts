plugins {
    id("com.example.spring-boot-application")
}

dependencies {
    implementation(projects.user.domain)
    implementation(projects.user.contract)
    implementation(projects.user.webmvc)
    implementation(projects.user.dataMongodb)
}
