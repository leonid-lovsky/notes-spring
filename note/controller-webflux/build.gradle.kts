plugins {
    id("com.example.spring-boot-webflux")
}

dependencies {
    implementation(project(":note:contract-common"))
    implementation(project(":note:contract-reactive"))
}
