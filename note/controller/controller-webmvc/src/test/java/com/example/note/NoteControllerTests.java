package com.example.note;

import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(NoteController.class)
@MockitoBean(types = {FindNoteByIdSynchronous.class, CreateNoteByIdSynchronous.class, ReplaceNoteByIdSynchronous.class, UpdateNoteByIdSynchronous.class, DeleteNoteByIdSynchronous.class})
class NoteControllerTests {

    @Test
    void contextLoads() {
        /* context startup is the assertion */
    }
}
