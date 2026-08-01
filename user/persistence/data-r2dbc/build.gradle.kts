plugins {
    id("com.example.spring-boot-data-r2dbc")
}

dependencies {
    implementation(projects.user.contractReactive)
    implementation(projects.user.domain)
}
