plugins {
    id("org.springframework.boot")
}

val mainClassFile = objects.fileProperty().fileValue(project.projectDir.parentFile.resolve(".main-class"))

springBoot {
    mainClass = providers.fileContents(mainClassFile).asText.map { it.trim() }
}
