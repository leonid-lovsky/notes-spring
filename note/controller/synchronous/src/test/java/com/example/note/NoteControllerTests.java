package com.example.note;

import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(NoteController.class)
@MockitoBean(types = {FindNoteById.class, CreateNoteById.class, ReplaceNoteById.class, UpdateNoteById.class, DeleteNoteById.class})
class NoteControllerTests {

    @Test
    void contextLoads() {
        /* context startup is the assertion */
    }
}
