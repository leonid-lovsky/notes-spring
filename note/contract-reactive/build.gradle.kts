plugins {
    id("com.example.library")
    id("com.example.reactor")
}

dependencies {
    api(projects.note.domain)
}
