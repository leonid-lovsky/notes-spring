package com.example.note;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(NoteTestConfiguration.class)
class NoteApplicationTests {

    @Test
    void contextLoads() {}
}
