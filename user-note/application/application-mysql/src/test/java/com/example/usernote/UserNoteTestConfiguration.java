package com.example.usernote;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.mysql.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
class UserNoteTestConfiguration {

    @Bean
    @ServiceConnection
    MySQLContainer mySQLContainer() {
        return new MySQLContainer(DockerImageName.parse("mysql:latest"));
    }
}
