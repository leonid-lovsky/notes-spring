pluginManagement {
    includeBuild("build-logic")
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "notes-spring"

include(":note:contract:contract-common")
include(":note:contract:contract-synchronous")
include(":note:contract:contract-reactive")

include(":note:controller:controller-webmvc")
include(":note:controller:controller-webflux")

include(":note:data:data-jdbc")
include(":note:data:data-r2dbc")
include(":note:data:data-mongodb")
include(":note:data:data-mongodb-reactive")

include(":note:application:application-h2")
include(":note:application:application-h2-reactive")
include(":note:application:application-mysql")
include(":note:application:application-mysql-reactive")
include(":note:application:application-postgresql")
include(":note:application:application-postgresql-reactive")
include(":note:application:application-mongodb")
include(":note:application:application-mongodb-reactive")

include(":user:contract:contract-common")
include(":user:contract:contract-synchronous")
include(":user:contract:contract-reactive")

include(":user:controller:controller-webmvc")
include(":user:controller:controller-webflux")

include(":user:data:data-jdbc")
include(":user:data:data-r2dbc")
include(":user:data:data-mongodb")
include(":user:data:data-mongodb-reactive")

include(":user:application:application-h2")
include(":user:application:application-h2-reactive")
include(":user:application:application-mysql")
include(":user:application:application-mysql-reactive")
include(":user:application:application-postgresql")
include(":user:application:application-postgresql-reactive")
include(":user:application:application-mongodb")
include(":user:application:application-mongodb-reactive")

include(":user-note:contract:contract-common")
include(":user-note:contract:contract-synchronous")
include(":user-note:contract:contract-reactive")

include(":user-note:controller:controller-webmvc")
include(":user-note:controller:controller-webflux")

include(":user-note:data:data-jdbc")
include(":user-note:data:data-r2dbc")
include(":user-note:data:data-mongodb")
include(":user-note:data:data-mongodb-reactive")

include(":user-note:application:application-h2")
include(":user-note:application:application-h2-reactive")
include(":user-note:application:application-mysql")
include(":user-note:application:application-mysql-reactive")
include(":user-note:application:application-postgresql")
include(":user-note:application:application-postgresql-reactive")
include(":user-note:application:application-mongodb")
include(":user-note:application:application-mongodb-reactive")
