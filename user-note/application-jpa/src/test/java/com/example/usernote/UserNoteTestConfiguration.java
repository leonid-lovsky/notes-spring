package com.example.usernote;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.testcontainers.mysql.MySQLContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
class UserNoteTestConfiguration {

    @Bean
    @Profile("MySQL")
    @ServiceConnection
    MySQLContainer mySQLContainer() {
        return new MySQLContainer(DockerImageName.parse("mysql:latest"));
    }

    @Bean
    @Profile("PostgreSQL")
    @ServiceConnection
    PostgreSQLContainer postgreSQLContainer() {
        return new PostgreSQLContainer(DockerImageName.parse("postgres:latest"));
    }
}
