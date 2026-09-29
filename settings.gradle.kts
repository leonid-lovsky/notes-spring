pluginManagement {
    includeBuild("build-logic")
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "notes-spring"

include(":note-user:application-h2")
include(":note-user:application-h2-reactive")
include(":note-user:application-mongodb")
include(":note-user:application-mongodb-reactive")
include(":note-user:application-mysql")
include(":note-user:application-mysql-reactive")
include(":note-user:application-postgresql")
include(":note-user:application-postgresql-reactive")

include(":note-user:contract")
include(":note-user:contract-reactive")

include(":note-user:controller-webmvc")
include(":note-user:controller-webflux")

include(":note-user:data-jdbc")
include(":note-user:data-mongodb")
include(":note-user:data-mongodb-reactive")
include(":note-user:data-r2dbc")
