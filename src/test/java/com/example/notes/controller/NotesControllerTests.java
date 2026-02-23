package com.example.notes.controller;

import com.example.notes.payload.NotesInput;
import com.example.notes.payload.NotesOutput;
import com.example.notes.repository.NotesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureRestTestClient
@DisplayName("Test notes controller")
class NotesControllerTests {

    @Autowired
    private RestTestClient restTestClient;

    @Autowired
    NotesRepository repository;

    @BeforeEach
    @DisplayName("Test notes repository is empty")
    void testNotesRepositoryIsEmpty() {
        repository.deleteAll();
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("Test greeting returns Hello World")
    void testGreetingReturnsHelloWorld() {
        var content = "Hello, World";

        restTestClient.get().uri("/greeting").exchange()
            .expectBody(String.class)
            .isEqualTo(content);
    }

    @Test
    @DisplayName("Test post valid data returns success result")
    void testPostValidDataReturnsSuccessResult() {
        var content = "Hello, World";

        var request = NotesInput.builder().content(content).build();

        restTestClient.post().uri("/").body(request).exchange()
            .expectStatus().isCreated()
            .expectHeader().contentType("application/json")
            .expectCookie().doesNotExist("JSESSIONID")
            .expectBody(NotesOutput.class)
            .value(result -> {
                assertThat(result).isNotNull();
                assertThat(result.id()).isNotNull();
                assertThat(result.content()).isEqualTo(content);
            });
    }
}
