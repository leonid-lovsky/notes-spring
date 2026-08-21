package com.example.usernote;

import org.testcontainers.mysql.MySQLContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@TestConfiguration(proxyBeanMethods = false)
class UserNoteTestConfigutation {

    @Bean
    @Profile("mysql")
    @ServiceConnection
    MySQLContainer mysqlContainer() {
        return new MySQLContainer("mysql:9");
    }

    @Bean
    @Profile("postgresql")
    @ServiceConnection
    PostgreSQLContainer postgreSQLContainer() {
        return new PostgreSQLContainer("postgres:18");
    }
}
