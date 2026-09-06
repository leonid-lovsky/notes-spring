pluginManagement {
    includeBuild("build-logic")
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "notes-spring"

include(":user-note:application-core")

include(":user-note:application-h2")
include(":user-note:application-mysql")
include(":user-note:application-postgresql")
include(":user-note:application-h2-reactive")
include(":user-note:application-mysql-reactive")
include(":user-note:application-postgresql-reactive")
include(":user-note:application-mongodb")
include(":user-note:application-mongodb-reactive")

include(":user-note:data-jdbc")
include(":user-note:data-jpa")
include(":user-note:data-mongodb")
include(":user-note:data-mongodb-reactive")
include(":user-note:data-r2dbc")
