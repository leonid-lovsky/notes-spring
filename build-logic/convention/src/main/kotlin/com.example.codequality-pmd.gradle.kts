plugins {
    id("pmd")
}

val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

pmd {
    toolVersion = libs.findVersion("pmd").get().requiredVersion
    ruleSets = emptyList()
    ruleSetFiles = rootProject.files("gradle/pmd/quickstart.xml")
}
