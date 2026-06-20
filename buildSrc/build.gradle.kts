plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

val springBootVersion = "4.0.6"
val dependencyManagementVersion = "1.1.7"

dependencies {
    implementation("org.springframework.boot:spring-boot-gradle-plugin:$springBootVersion")
    implementation("io.spring.gradle:dependency-management-plugin:$dependencyManagementVersion")
}
