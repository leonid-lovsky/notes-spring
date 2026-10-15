plugins {
    id("com.example.service-synchronous")
    id("com.example.spring-boot")
}

dependencies {
    api(project(":user-note:contract:imperative"))
}
