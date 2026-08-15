pluginManagement {
    includeBuild("build-logic")
}

rootProject.name = "notes-spring"

include(":user-note:application:JDBC:JDBC-H2")
include(":user-note:application:JDBC:JDBC-MySQL")
include(":user-note:application:JDBC:JDBC-PostgreSQL")

include(":user-note:application:JPA:JPA-H2")
include(":user-note:application:JPA:JPA-MySQL")
include(":user-note:application:JPA:JPA-PostgreSQL")

include(":user-note:application:MongoDB:MongoDB")
include(":user-note:application:MongoDB:MongoDB-reactive")

include(":user-note:application:R2DBC:R2DBC-H2")
include(":user-note:application:R2DBC:R2DBC-MySQL")
include(":user-note:application:R2DBC:R2DBC-PostgreSQL")
