plugins {
    id("pmd")
}

val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

pmd {
    toolVersion = libs.findVersion("pmd").get().requiredVersion
}
