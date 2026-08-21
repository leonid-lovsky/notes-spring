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
    class H2 extends DatabaseProductNameTests {

        @Override
        String expectedProductName() {
            return "H2";
        }
    }

    @Nested
    @ActiveProfiles("mysql")
    @Testcontainers
    class MySQL extends DatabaseProductNameTests {

        @Container
        @ServiceConnection
        static MySQLContainer mySQLContainer = new MySQLContainer("mysql:9");

        @Override
        String expectedProductName() {
            return "MySQL";
        }
    }

    @Nested
    @ActiveProfiles("postgresql")
    @Testcontainers
    class PostgreSQL extends DatabaseProductNameTests {

        @Container
        @ServiceConnection
        static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:18");

        @Override
        String expectedProductName() {
            return "PostgreSQL";
        }
    }

    abstract static class DatabaseProductNameTests {

        @Autowired
        DataSource dataSource;

        abstract String expectedProductName();

        @Test
        void contextLoads() throws SQLException {
            Assertions
                .assertThat(this.dataSource.getConnection().getMetaData().getDatabaseProductName())
                .isEqualTo(this.expectedProductName());
        }
    }
}
