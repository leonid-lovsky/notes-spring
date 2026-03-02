package com.example.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;

@SpringBootTest
@DisplayName("Application Tests")
class NotesApplicationTests {

    @Test
    @DisplayName("Context loads")
    void contextLoads() {
        ApplicationModules modules = ApplicationModules.of(NotesApplication.class);
        modules.forEach(System.out::println);
        modules.verify();
    }
}
