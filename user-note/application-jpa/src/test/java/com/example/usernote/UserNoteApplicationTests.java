package com.example.usernote;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Import(UserNoteTestConfiguration.class)
class UserNoteApplicationTests {

    abstract static class DatabaseProductNameTests {

        @Autowired
        DataSource dataSource;

        abstract String expectedDatabaseProductName();

        @Test
        void contextLoads() throws SQLException {
            Assertions
                .assertThat(this.dataSource.getConnection().getMetaData().getDatabaseProductName())
                .isEqualTo(this.expectedDatabaseProductName());
        }
    }

    @Nested
    @ActiveProfiles("h2")
    class H2 extends DatabaseProductNameTests {

        @Override
        String expectedDatabaseProductName() {
            return "H2";
        }
    }

    @Nested
    @ActiveProfiles("mysql")
    class MySQL extends DatabaseProductNameTests {

        @Override
        String expectedDatabaseProductName() {
            return "MySQL";
        }
    }

    @Nested
    @ActiveProfiles("postgresql")
    class PostgreSQL extends DatabaseProductNameTests {

        @Override
        String expectedDatabaseProductName() {
            return "PostgreSQL";
        }
    }
}
