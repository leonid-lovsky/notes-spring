plugins {
    id("checkstyle")
}

val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

checkstyle {
    toolVersion = libs.findVersion("checkstyle").get().requiredVersion
    configFile = rootProject.file("gradle/checkstyle/google_checks.xml")
}

dependencies {
    checkstyle("com.puppycrawl.tools:checkstyle:${libs.findVersion("checkstyle").get().requiredVersion}")
}
