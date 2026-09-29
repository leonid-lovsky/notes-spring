pluginManagement {
    includeBuild("build-logic")
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "notes-spring"

include(":note:contract")
include(":note:contract-reactive")

include(":note:controller-webmvc")
include(":note:controller-webflux")

include(":note:data-jdbc")
include(":note:data-r2dbc")
include(":note:data-mongodb")
include(":note:data-mongodb-reactive")

include(":note:application-h2")
include(":note:application-h2-reactive")
include(":note:application-mysql")
include(":note:application-mysql-reactive")
include(":note:application-postgresql")
include(":note:application-postgresql-reactive")
include(":note:application-mongodb")
include(":note:application-mongodb-reactive")

include(":user:contract")
include(":user:contract-reactive")

include(":user:controller-webmvc")
include(":user:controller-webflux")

include(":user:data-jdbc")
include(":user:data-r2dbc")
include(":user:data-mongodb")
include(":user:data-mongodb-reactive")

include(":user:application-h2")
include(":user:application-h2-reactive")
include(":user:application-mysql")
include(":user:application-mysql-reactive")
include(":user:application-postgresql")
include(":user:application-postgresql-reactive")
include(":user:application-mongodb")
include(":user:application-mongodb-reactive")

include(":user-note:contract")
include(":user-note:contract-reactive")

include(":user-note:controller-webmvc")
include(":user-note:controller-webflux")

include(":user-note:data-jdbc")
include(":user-note:data-r2dbc")
include(":user-note:data-mongodb")
include(":user-note:data-mongodb-reactive")

include(":user-note:application-h2")
include(":user-note:application-h2-reactive")
include(":user-note:application-mysql")
include(":user-note:application-mysql-reactive")
include(":user-note:application-postgresql")
include(":user-note:application-postgresql-reactive")
include(":user-note:application-mongodb")
include(":user-note:application-mongodb-reactive")
