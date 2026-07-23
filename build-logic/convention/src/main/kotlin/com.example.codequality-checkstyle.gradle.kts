plugins {
    id("checkstyle")
}

val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

repositories {
    maven { url = uri("https://repo.spring.io/snapshot") }
}

checkstyle {
    toolVersion = libs.findVersion("checkstyle").get().requiredVersion
    configFile = rootProject.file("gradle/checkstyle/checkstyle.xml")
    configProperties = mapOf("projectRootPackage" to "com.example")
}

dependencies {
    checkstyle("com.puppycrawl.tools:checkstyle:${libs.findVersion("checkstyle").get().requiredVersion}")
    checkstyle("io.spring.javaformat:spring-javaformat-checkstyle:${libs.findVersion("spring-javaformat").get().requiredVersion}")
}
