pluginManagement {
    includeBuild("build-logic")
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "notes-spring"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":auth:application")

include(":config:application")

include(":gateway:application")

include(":registry:application")

include(":note:application-jpa")
include(":note:application-r2dbc")
include(":note:domain")
include(":note:contract")
include(":note:contract-reactive")
include(":note:webmvc")
include(":note:webflux")
include(":note:data-jpa")
include(":note:data-mongodb")
include(":note:data-jdbc")
include(":note:data-r2dbc")
include(":note:data-mongodb-reactive")

include(":user:application-jpa")
include(":user:application-r2dbc")
include(":user:domain")
include(":user:contract")
include(":user:contract-reactive")
include(":user:webmvc")
include(":user:webflux")
include(":user:data-jpa")
include(":user:data-mongodb")
include(":user:data-jdbc")
include(":user:data-r2dbc")
include(":user:data-mongodb-reactive")

include(":user-note:application-jpa")
include(":user-note:application-r2dbc")
include(":user-note:domain")
include(":user-note:contract")
include(":user-note:contract-reactive")
include(":user-note:webmvc")
include(":user-note:webflux")
include(":user-note:data-jpa")
include(":user-note:data-mongodb")
include(":user-note:data-jdbc")
include(":user-note:data-r2dbc")
include(":user-note:data-mongodb-reactive")
