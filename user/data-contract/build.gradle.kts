plugins {
    id("com.example.library")
}

dependencies {
    api(projects.user.domain)
}
