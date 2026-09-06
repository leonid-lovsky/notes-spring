plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-gradle-plugin:${libs.versions.spring.boot.get()}")
    implementation("io.spring.gradle:dependency-management-plugin:${libs.versions.spring.dependency.management.get()}")
    implementation("net.ltgt.gradle:gradle-errorprone-plugin:${libs.versions.errorprone.plugin.get()}")
    implementation("com.diffplug.spotless:spotless-plugin-gradle:${libs.versions.spotless.get()}")
    implementation("com.github.spotbugs:com.github.spotbugs.gradle.plugin:${libs.versions.spotbugs.plugin.get()}")
}
