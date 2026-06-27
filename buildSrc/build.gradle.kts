plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

val springBootVersion = "4.0.6"
val dependencyManagementVersion = "1.1.7"
val springJavaformatVersion = "0.0.47"
val errorpronePluginVersion = "5.1.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-gradle-plugin:$springBootVersion")
    implementation("io.spring.gradle:dependency-management-plugin:$dependencyManagementVersion")
    implementation("io.spring.javaformat:spring-javaformat-gradle-plugin:$springJavaformatVersion")
    implementation("net.ltgt.gradle:gradle-errorprone-plugin:$errorpronePluginVersion")
}
