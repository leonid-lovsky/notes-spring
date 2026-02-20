package com.example.notes.controller;

import com.example.notes.payload.NotesInput;
import com.example.notes.payload.NotesOutput;
import com.example.notes.service.NotesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@WebMvcTest(NotesController.class)
@AutoConfigureRestTestClient
class NotesControllerTests {

    @Autowired
    private RestTestClient restTestClient;

    @MockitoBean
    private NotesService service;

    @Test
    void greetingShouldReturnDefaultMessage() {
        var content = "Hello, World";

        when(service.greet()).thenReturn(content);

        restTestClient.get().uri("/greeting")
            .exchange()
            .expectBody(String.class)
            .isEqualTo(content);
    }

    @Test
    void create() {
        var content = "Hello, World";
        var uuid = UUID.randomUUID();

        var request = NotesInput.builder().content(content).build();
        var response = NotesOutput.builder().id(uuid).content(content).build();

        when(service.create(request)).thenReturn(response);

        restTestClient.post().uri("/")
            .body(request)
            .exchange()
            .expectBody(NotesOutput.class)
            .value(result -> {
                assertThat(result).isNotNull();
                assertThat(result.id()).isEqualTo(uuid);
                assertThat(result.content()).isEqualTo(content);
            })
            .isEqualTo(response);
    }
}
