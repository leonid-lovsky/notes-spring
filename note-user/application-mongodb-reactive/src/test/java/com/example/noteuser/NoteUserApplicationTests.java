package com.example.noteuser;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(NoteUserTestConfiguration.class)
class NoteUserApplicationTests {

    @Test
    void contextLoads() {}
}
