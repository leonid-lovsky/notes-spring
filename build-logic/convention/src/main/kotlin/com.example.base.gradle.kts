plugins {
    id("java")
    id("com.example.codequality")
}

val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(
            providers.fileContents(rootProject.layout.projectDirectory.file(".java-version")).asText.get().trim().toInt()
        )
    }
}

dependencies {
    implementation("jakarta.validation:jakarta.validation-api:${libs.findVersion("jakarta-validation").get().requiredVersion}")
    testImplementation("org.junit.jupiter:junit-jupiter:${libs.findVersion("junit-jupiter").get().requiredVersion}")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:${libs.findVersion("junit-platform").get().requiredVersion}")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
