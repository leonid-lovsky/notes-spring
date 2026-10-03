plugins {
    id("com.example.spring-boot-webflux")
}

dependencies {
    implementation(project(":user-note:contract:contract-commons"))
    implementation(project(":user-note:contract:contract-reactive"))
}
