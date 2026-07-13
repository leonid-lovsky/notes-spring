plugins {
    id("com.example.library")
    id("com.example.reactor")
}

dependencies {
    api(projects.userNote.domain)
}
