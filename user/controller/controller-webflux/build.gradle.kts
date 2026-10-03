plugins {
    id("com.example.spring-boot-webflux")
}

dependencies {
    implementation(project(":user:contract:contract-commons"))
    implementation(project(":user:contract:contract-reactive"))
}
