pluginManagement {
    includeBuild("build-logic")
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "notes-spring"

include(":note:contract:contract-commons")
include(":note:contract:contract-synchronous")
include(":note:contract:contract-reactive")

include(":note:service:service-commons")
include(":note:service:service-synchronous")
include(":note:service:service-reactive")

include(":note:controller:controller-webmvc")
include(":note:controller:controller-webflux")

include(":note:application:application-h2")
include(":note:application:application-h2-reactive")
include(":note:application:application-mysql")
include(":note:application:application-mysql-reactive")
include(":note:application:application-postgresql")
include(":note:application:application-postgresql-reactive")
include(":note:application:application-mongodb")
include(":note:application:application-mongodb-reactive")

include(":user:contract:contract-commons")
include(":user:contract:contract-synchronous")
include(":user:contract:contract-reactive")

include(":user:service:service-commons")
include(":user:service:service-synchronous")
include(":user:service:service-reactive")

include(":user:controller:controller-webmvc")
include(":user:controller:controller-webflux")

include(":user:application:application-h2")
include(":user:application:application-h2-reactive")
include(":user:application:application-mysql")
include(":user:application:application-mysql-reactive")
include(":user:application:application-postgresql")
include(":user:application:application-postgresql-reactive")
include(":user:application:application-mongodb")
include(":user:application:application-mongodb-reactive")

include(":user-note:contract:contract-commons")
include(":user-note:contract:contract-synchronous")
include(":user-note:contract:contract-reactive")

include(":user-note:service:service-synchronous")
include(":user-note:service:service-reactive")

include(":user-note:controller:controller-webmvc")
include(":user-note:controller:controller-webflux")

include(":user-note:application:application-h2")
include(":user-note:application:application-h2-reactive")
include(":user-note:application:application-mysql")
include(":user-note:application:application-mysql-reactive")
include(":user-note:application:application-postgresql")
include(":user-note:application:application-postgresql-reactive")
include(":user-note:application:application-mongodb")
include(":user-note:application:application-mongodb-reactive")
