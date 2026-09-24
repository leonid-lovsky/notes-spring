plugins {
    id("com.example.base")
}

val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

dependencies {
    implementation("io.projectreactor:reactor-core:${libs.findVersion("reactor-core").get().requiredVersion}")
    testImplementation("io.projectreactor:reactor-test:${libs.findVersion("reactor-core").get().requiredVersion}")

    implementation("io.projectreactor:reactor-tools:${libs.findVersion("reactor-core").get().requiredVersion}")
}
