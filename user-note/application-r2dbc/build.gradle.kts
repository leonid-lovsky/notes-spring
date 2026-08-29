plugins {
    id("com.example.spring-boot-application")
    id("com.example.spring-boot-webflux")

    id("com.example.spring-boot-data-r2dbc")

    id("com.example.spring-boot-database-r2dbc-h2")
    id("com.example.spring-boot-database-r2dbc-mysql")
    id("com.example.spring-boot-database-r2dbc-postgresql")

    id("com.example.spring-boot-testcontainers")
    id("com.example.spring-boot-testcontainers-r2dbc")
    id("com.example.spring-boot-testcontainers-mysql")
    id("com.example.spring-boot-testcontainers-postgresql")
}
