pluginManagement {
    includeBuild("build-logic")
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "notes-spring"

include(":user-note:application-jdbc")
include(":user-note:application-jpa")
include(":user-note:application-mongodb")
include(":user-note:application-mongodb-reactive")
include(":user-note:application-r2dbc")
