plugins {
    id("com.example.spring-boot-webflux")
}

dependencies {
    implementation(project(":user:contract-common"))
    implementation(project(":user:contract-reactive"))
}
