package com.example.note;

import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

@WebMvcTest(NoteController.class)
class NoteControllerTests {

    @Test
    void contextLoads() {
        /* context startup is the assertion */
    }
}
