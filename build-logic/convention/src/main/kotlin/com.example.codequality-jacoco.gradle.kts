plugins {
    id("jacoco")
}

val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

jacoco {
    toolVersion = libs.findVersion("jacoco").get().requiredVersion
}

tasks.named("test") {
    finalizedBy(tasks.named("jacocoTestReport")) // report is always generated after tests run
}

tasks.named("jacocoTestReport") {
    dependsOn(tasks.named("test")) // tests are required to run before generating the report
}
