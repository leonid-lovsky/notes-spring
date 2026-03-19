package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;

@SpringBootTest
@DisplayName("Application Tests")
class NotesApplicationTests {

    @Test
    @DisplayName("Application modules are valid")
    void modulesAreValid() {
        ApplicationModules modules = ApplicationModules.of(NotesApplication.class);
        modules.verify();
    }

    @Test
    @DisplayName("Application context starts")
    void contextLoads() {
    }
}
