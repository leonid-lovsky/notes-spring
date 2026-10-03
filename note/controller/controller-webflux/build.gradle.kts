plugins {
    id("com.example.spring-boot-webflux")
}

dependencies {
    implementation(project(":note:contract:contract-commons"))
    implementation(project(":note:contract:contract-reactive"))
}
