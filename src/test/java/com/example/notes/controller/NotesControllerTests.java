package com.example.notes.controller;

import com.example.notes.model.NotesEntity;
import com.example.notes.payload.NotesInput;
import com.example.notes.payload.NotesOutput;
import com.example.notes.repository.NotesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureRestTestClient
@DisplayName("Test notes controller")
class NotesControllerTests {

    @Autowired
    private RestTestClient restTestClient;

    @Autowired
    private NotesRepository repository;

    @BeforeEach
    @DisplayName("Test repository is empty")
    void testDatabaseIsEmpty() {
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
    @DisplayName("Test post data returns data")
    void testPostDataReturnsData() {
        var content = "Hello, World";

        var request = NotesInput.builder().content(content).build();

        restTestClient.post().uri("/").body(request).exchange()
            .expectStatus().isCreated()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectCookie().doesNotExist("JSESSIONID")
            .expectBody(NotesOutput.class)
            .value(result -> {
                assertThat(result).isNotNull();
                assertThat(result.id()).isNotNull();
                assertThat(result.content()).isEqualTo(content);
            });
    }

    @Test
    @DisplayName("Test get data by id returns data")
    void testGetDataByIdReturnsData() {
        var content = "Hello, World";

        var entity = NotesEntity.builder().content(content).build();
        repository.save(entity);

        restTestClient.get().uri("/{id}", entity.getId()).exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectCookie().doesNotExist("JSESSIONID")
            .expectBody(NotesOutput.class)
            .value(result -> {
                assertThat(result).isNotNull();
                assertThat(result.id()).isEqualTo(entity.getId());
                assertThat(result.content()).isEqualTo(content);
            });
    }

    @Test
    @DisplayName("Test get data by id returns error if not exists")
    void testGetDataByIdReturnsErrorIfNotExists() {
        var uuid = UUID.randomUUID();

        restTestClient.get().uri("/{id}", uuid).exchange()
            .expectStatus().isNotFound()
            // .expectHeader().contentType(MediaType.APPLICATION_JSON)
            // .expectCookie().doesNotExist("JSESSIONID")
            .expectBody(ProblemDetail.class);
    }
}
