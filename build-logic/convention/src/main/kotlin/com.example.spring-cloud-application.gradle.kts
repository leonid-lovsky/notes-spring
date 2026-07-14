import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
    id("com.example.spring-boot-application")
}

val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${libs.findVersion("spring-cloud").get().requiredVersion}")
    }
}