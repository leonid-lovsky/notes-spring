package com.example.usernote;

import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(UserNoteController.class)
@MockitoBean(types = {FindUserNoteByIdSynchronous.class, CreateUserNoteByIdSynchronous.class, ReplaceUserNoteByIdSynchronous.class, UpdateUserNoteByIdSynchronous.class, DeleteUserNoteByIdSynchronous.class})
class UserNoteControllerTests {

    @Test
    void contextLoads() {
        /* context startup is the assertion */
    }
}
