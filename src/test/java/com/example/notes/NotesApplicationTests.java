package com.example.notes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;

@SpringBootTest
@DisplayName("Test notes application")
class NotesApplicationTests {

    @Test
    @DisplayName("Test context loads")
    void testContextLoads() {
        ApplicationModules.of(NotesApplication.class).verify();
    }
}
