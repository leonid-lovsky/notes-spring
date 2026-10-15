plugins {
    id("com.example.service-reactive")
    id("com.example.spring-boot")
}

dependencies {
    api(project(":user-note:contract:reactive"))
}
