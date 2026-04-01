package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;

@SpringBootTest
class NotesApplicationTest {

    @Test
    void contextLoads() {
        ApplicationModules.of(NotesApplication.class).verify();
    }
}
