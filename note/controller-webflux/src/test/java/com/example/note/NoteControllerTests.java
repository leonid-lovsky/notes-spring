package com.example.note;

import org.junit.jupiter.api.Test;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;

@WebFluxTest(NoteController.class)
class NoteControllerTests {

    @Test
    void contextLoads() {
        /* context startup is the assertion */
    }
}
