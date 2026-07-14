plugins {
    id("java-library")
}

val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

dependencies {
    api("org.jspecify:jspecify:${libs.findVersion("jspecify").get().requiredVersion}")
}
