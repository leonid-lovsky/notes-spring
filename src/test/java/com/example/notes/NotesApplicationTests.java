package com.example.notes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;

@SpringBootTest
@DisplayName("Notes application")
class NotesApplicationTests {

    @Test
    @DisplayName("Context loads")
    void contextLoads() {
        ApplicationModules.of(NotesApplication.class).verify();
    }
}
