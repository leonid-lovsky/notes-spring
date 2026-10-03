package com.example.note;

import org.junit.jupiter.api.Test;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebFluxTest(ReactiveNoteController.class)
@MockitoBean(types = {ReactiveFindNoteById.class, ReactiveCreateNoteById.class, ReactiveReplaceNoteById.class, ReactiveUpdateNoteById.class, ReactiveDeleteNoteById.class})
class ReactiveNoteControllerTests {

    @Test
    void contextLoads() {
        /* context startup is the assertion */
    }
}
