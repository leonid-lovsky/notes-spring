plugins {
    id("com.example.commons")
    id("java-library")
}

val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

dependencies {
    compileOnlyApi("org.mapstruct:mapstruct:${libs.findVersion("mapstruct").get().requiredVersion}")
    annotationProcessor("org.mapstruct:mapstruct-processor:${libs.findVersion("mapstruct").get().requiredVersion}")
}
