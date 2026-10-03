package com.example.usernote;

import org.junit.jupiter.api.Test;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebFluxTest(UserNoteController.class)
@MockitoBean(types = {ReactiveFindUserNoteById.class, ReactiveCreateUserNoteById.class, ReactiveReplaceUserNoteById.class, ReactiveUpdateUserNoteById.class, ReactiveDeleteUserNoteById.class})
class UserNoteControllerTests {

    @Test
    void contextLoads() {
        /* context startup is the assertion */
    }
}
