package com.example.note;

import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;

@SpringBootTest
@Testcontainers
class NoteMysqlJdbcApplicationTests {

    @Container
    @ServiceConnection
    static MySQLContainer mySQLContainer = new MySQLContainer("mysql:9");

    @Test
    void contextLoads() {

    }
}
