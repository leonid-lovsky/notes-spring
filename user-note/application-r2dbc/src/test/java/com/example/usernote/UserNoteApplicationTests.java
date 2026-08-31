package com.example.usernote;

import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Import(UserNoteTestConfiguration.class)
class UserNoteApplicationTests {

    abstract static class DatabaseClientTests {

        @Autowired
        DatabaseClient databaseClient;

        abstract String expectedDatabaseProductName();

        @Test
        void contextLoads() {
            String databaseProductName = this.databaseClient
                .inConnection(connection -> Mono.just(connection.getMetadata().getDatabaseProductName()))
                .block();
            Assertions.assertThat(databaseProductName).contains(this.expectedDatabaseProductName());
        }
    }

    @Nested
    @ActiveProfiles("h2")
    class H2 extends DatabaseClientTests {

        @Override
        String expectedDatabaseProductName() {
            return "H2";
        }
    }

    @Nested
    @ActiveProfiles("mysql")
    class MySQL extends DatabaseClientTests {

        @Override
        String expectedDatabaseProductName() {
            return "MySQL";
        }
    }

    @Nested
    @ActiveProfiles("postgresql")
    class PostgreSQL extends DatabaseClientTests {

        @Override
        String expectedDatabaseProductName() {
            return "PostgreSQL";
        }
    }
}
