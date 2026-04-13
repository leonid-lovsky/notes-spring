package com.example.application;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;

@SpringBootTest
class ApplicationTests {

    @Test
    void contextLoads() {
        ApplicationModules.of(Application.class).verify();
    }
}
