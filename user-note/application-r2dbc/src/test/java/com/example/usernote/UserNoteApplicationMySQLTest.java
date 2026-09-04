package com.example.usernote;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("MySQL")
@Import(UserNoteTestConfiguration.class)
class UserNoteApplicationMySQLTest {

    @Test
    void contextLoads() {}
}
