package com.example.notes.controller;

import com.example.notes.model.NotesEntity;
import com.example.notes.payload.NotesPayload;
import com.example.notes.payload.NotesResponse;
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
@DisplayName("Notes controller")
class NotesControllerTests {

    @Autowired
    private RestTestClient restTestClient;

    @Autowired
    private NotesRepository repository;

    @BeforeEach
    @DisplayName("Repository is empty")
    void repositoryIsEmpty() {
        repository.deleteAll();
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("Greeting returns Hello World")
    void greetingReturnsHelloWorld() {
        var content = "Hello, World";

        restTestClient.get().uri("/greeting").exchange()
            .expectBody(String.class)
            .isEqualTo(content);
    }

    @Test
    @DisplayName("Post data returns data")
    void postDataReturnsData() {
        var content = "Hello, World";

        var request = NotesPayload.builder().content(content).build();

        restTestClient.post().uri("/").body(request).exchange()
            .expectStatus().isCreated()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectCookie().doesNotExist("JSESSIONID")
            .expectBody(NotesResponse.class)
            .value(result -> {
                assertThat(result).isNotNull();
                assertThat(result.id()).isNotNull();
                assertThat(result.content()).isEqualTo(content);
            });
    }

    @Test
    @DisplayName("Get data by id returns data")
    void getDataByIdReturnsData() {
        var content = "Hello, World";

        var entity = NotesEntity.builder().content(content).build();
        repository.save(entity);

        restTestClient.get().uri("/{id}", entity.getId()).exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectCookie().doesNotExist("JSESSIONID")
            .expectBody(NotesResponse.class)
            .value(result -> {
                assertThat(result).isNotNull();
                assertThat(result.id()).isEqualTo(entity.getId());
                assertThat(result.content()).isEqualTo(content);
            });
    }

    @Test
    @DisplayName("Get data by id returns error if not exists")
    void getDataByIdReturnsErrorIfNotExists() {
        var uuid = UUID.randomUUID();

        restTestClient.get().uri("/{id}", uuid).exchange()
            .expectStatus().isNotFound()
            .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .expectCookie().doesNotExist("JSESSIONID")
            .expectBody(ProblemDetail.class)
            .value(System.out::println);
    }
}
