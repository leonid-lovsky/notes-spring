plugins {
    id("com.example.java-library")
    id("com.example.project-reactor")
}

dependencies {
    api(projects.note.domain)
}
