package com.example.usernote;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
class UserNoteApplicationTests {

    @Nested
    @ActiveProfiles("h2")
    class H2 {

        @Autowired
        DataSource dataSource;

        @Test
        void contextLoads() throws SQLException {
            Assertions
                .assertThat(this.dataSource.getConnection().getMetaData().getDatabaseProductName())
                .isEqualTo("H2");
        }
    }

    @Nested
    @ActiveProfiles("mysql")
    @Testcontainers
    class Mysql {

        @Container
        @ServiceConnection
        static MySQLContainer mysqlContainer = new MySQLContainer("mysql:9");

        @Autowired
        DataSource dataSource;

        @Test
        void contextLoads() throws SQLException {
            Assertions
                .assertThat(this.dataSource.getConnection().getMetaData().getDatabaseProductName())
                .isEqualTo("MySQL");
        }
    }

    @Nested
    @ActiveProfiles("postgresql")
    @Testcontainers
    class Postgresql {

        @Container
        @ServiceConnection
        static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer("postgres:18");

        @Autowired
        DataSource dataSource;

        @Test
        void contextLoads() throws SQLException {
            Assertions
                .assertThat(this.dataSource.getConnection().getMetaData().getDatabaseProductName())
                .isEqualTo("PostgreSQL");
        }
    }
}
