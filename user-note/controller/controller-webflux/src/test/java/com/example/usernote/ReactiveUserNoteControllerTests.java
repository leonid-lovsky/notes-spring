package com.example.usernote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@WebFluxTest(ReactiveUserNoteController.class)
class ReactiveUserNoteControllerTests {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private ReactiveUserNoteService userNoteService;

    @Test
    void findUserNoteByIdReturnsOk() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNoteResponseBody responseBody = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.EDITOR);
        when(userNoteService.findUserNoteById(id)).thenReturn(Mono.just(responseBody));

        String expected = "{\"id\":\"%s\",\"userId\":\"%s\",\"noteId\":\"%s\",\"role\":\"EDITOR\"}".formatted(id, userId, noteId);

        webTestClient.get().uri("/user-notes/{id}", id).exchange()
            .expectStatus().isOk()
            .expectBody(String.class).isEqualTo(expected);
    }

    @Test
    void createUserNoteReturnsOk() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNoteRequestBody requestBody = new UserNoteRequestBody(userId, noteId, UserNoteRole.EDITOR);
        UserNoteResponseBody responseBody = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.EDITOR);
        String content = "{\"userId\":\"%s\",\"noteId\":\"%s\",\"role\":\"EDITOR\"}".formatted(userId, noteId);
        when(userNoteService.createUserNote(requestBody)).thenReturn(Mono.just(responseBody));

        String expected = "{\"id\":\"%s\",\"userId\":\"%s\",\"noteId\":\"%s\",\"role\":\"EDITOR\"}".formatted(id, userId, noteId);

        webTestClient.post().uri("/user-notes").contentType(MediaType.APPLICATION_JSON).bodyValue(content).exchange()
            .expectStatus().isOk()
            .expectBody(String.class).isEqualTo(expected);
    }

    @Test
    void createUserNoteWithoutUserIdReturnsBadRequest() {
        UUID noteId = UUID.randomUUID();

        String content = "{\"noteId\":\"%s\",\"role\":\"EDITOR\"}".formatted(noteId);

        webTestClient.post().uri("/user-notes").contentType(MediaType.APPLICATION_JSON).bodyValue(content).exchange()
            .expectStatus().isBadRequest();

        verifyNoInteractions(userNoteService);
    }

    @Test
    void updateUserNoteRoleByIdReturnsOk() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNoteResponseBody responseBody = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.VIEWER);
        when(userNoteService.updateUserNoteRoleById(id, UserNoteRole.VIEWER)).thenReturn(Mono.just(responseBody));

        String expected = "{\"id\":\"%s\",\"userId\":\"%s\",\"noteId\":\"%s\",\"role\":\"VIEWER\"}".formatted(id, userId, noteId);

        webTestClient.patch().uri("/user-notes/{id}", id).contentType(MediaType.APPLICATION_JSON).bodyValue("\"VIEWER\"").exchange()
            .expectStatus().isOk()
            .expectBody(String.class).isEqualTo(expected);
    }

    @Test
    void deleteUserNoteByIdReturnsOk() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNoteResponseBody responseBody = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.EDITOR);
        when(userNoteService.deleteUserNoteById(id)).thenReturn(Mono.just(responseBody));

        String expected = "{\"id\":\"%s\",\"userId\":\"%s\",\"noteId\":\"%s\",\"role\":\"EDITOR\"}".formatted(id, userId, noteId);

        webTestClient.delete().uri("/user-notes/{id}", id).exchange()
            .expectStatus().isOk()
            .expectBody(String.class).isEqualTo(expected);
    }
}
