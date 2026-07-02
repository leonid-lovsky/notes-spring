import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
    id("java")
    id("io.spring.javaformat")
    id("checkstyle")
}

val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

repositories {
    mavenCentral()
}

checkstyle {
    toolVersion = libs.findVersion("checkstyle").get().requiredVersion
    configFile = rootProject.file("gradle/checkstyle/checkstyle.xml")
    configProperties = mapOf("projectRootPackage" to "com.example")
}

dependencies {
    checkstyle("io.spring.javaformat:spring-javaformat-checkstyle:${libs.findVersion("spring-javaformat").get().requiredVersion}")
}
