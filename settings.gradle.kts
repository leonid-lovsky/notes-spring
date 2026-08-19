pluginManagement {
    includeBuild("build-logic")
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "notes-spring"

include(":user-note:application:JDBC")

include(":user-note:application:JPA")

include(":user-note:application:MongoDB")
include(":user-note:application:MongoDB-reactive")

include(":user-note:application:R2DBC")
