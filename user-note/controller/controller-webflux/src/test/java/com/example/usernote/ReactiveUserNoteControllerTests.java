package com.example.usernote;

import org.junit.jupiter.api.Test;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebFluxTest(ReactiveUserNoteController.class)
@MockitoBean(types = {ReactiveFindUserNoteById.class, ReactiveCreateUserNoteById.class, ReactiveReplaceUserNoteById.class, ReactiveUpdateUserNoteById.class, ReactiveDeleteUserNoteById.class})
class ReactiveUserNoteControllerTests {

    @Test
    void contextLoads() {
        /* context startup is the assertion */
    }
}
