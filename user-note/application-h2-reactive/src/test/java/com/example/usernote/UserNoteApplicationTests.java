package com.example.usernote;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(UserNoteTestConfiguration.class)
class UserNoteApplicationTests {

    @Test
    void contextLoads() {}
}
