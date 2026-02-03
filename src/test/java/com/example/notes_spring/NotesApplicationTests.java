package com.example.notes_spring;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;

@SpringBootTest
class NotesApplicationTests {

    @Test
    void contextLoads() {
        ApplicationModules.of(NotesApplication.class).verify();
    }
}
