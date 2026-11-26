package com.example.notes;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.testcontainers.mysql.MySQLContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    @Profile("mysql")
    MySQLContainer mysqlContainer() {
        DockerImageName mysqlImage = DockerImageName.parse("mysql:9.7");
        return new MySQLContainer(mysqlImage);
    }

    @Bean
    @ServiceConnection
    @Profile("postgresql")
    PostgreSQLContainer postgresContainer() {
        DockerImageName postgresImage = DockerImageName.parse("postgres:18");
        return new PostgreSQLContainer(postgresImage);
    }
}
