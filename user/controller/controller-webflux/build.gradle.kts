plugins {
    id("com.example.spring-boot-webflux")
}

dependencies {
    implementation(project(":user:contract:contract-common"))
    implementation(project(":user:contract:contract-reactive"))
}
