plugins {
    id("java")
}

val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

dependencies {
    implementation("org.jspecify:jspecify:${libs.findVersion("jspecify").get().requiredVersion}")
}
