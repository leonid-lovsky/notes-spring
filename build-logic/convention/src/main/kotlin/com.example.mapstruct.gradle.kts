plugins {
    id("com.example.commons")
}

val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

dependencies {
    compileOnly("org.mapstruct:mapstruct:${libs.findVersion("mapstruct").get().requiredVersion}")
    annotationProcessor("org.mapstruct:mapstruct-processor:${libs.findVersion("mapstruct").get().requiredVersion}")
}
