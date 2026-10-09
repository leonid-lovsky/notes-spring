package com.example.usernote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@WebFluxTest(ReactiveUserNoteController.class)
@AutoConfigureWebTestClient
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

        webTestClient.get().uri("/user-notes/{id}", id).exchange()
            .expectStatus().isOk()
            .expectBody(UserNoteResponseBody.class).isEqualTo(responseBody);
    }

    @Test
    void createUserNoteReturnsOk() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNoteRequestBody requestBody = new UserNoteRequestBody(userId, noteId, UserNoteRole.EDITOR);
        UserNoteResponseBody responseBody = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.EDITOR);
        when(userNoteService.createUserNote(requestBody)).thenReturn(Mono.just(responseBody));

        webTestClient.post().uri("/user-notes").contentType(MediaType.APPLICATION_JSON).bodyValue(requestBody).exchange()
            .expectStatus().isOk()
            .expectBody(UserNoteResponseBody.class).isEqualTo(responseBody);
    }

    @Test
    void createUserNoteWithoutUserIdReturnsBadRequest() {
        UUID noteId = UUID.randomUUID();

        Map<String, Object> requestBody = Map.of("noteId", noteId, "role", UserNoteRole.EDITOR);

        webTestClient.post().uri("/user-notes").contentType(MediaType.APPLICATION_JSON).bodyValue(requestBody).exchange()
            .expectStatus().isBadRequest();

        verifyNoInteractions(userNoteService);
    }

    @Test
    void updateUserNoteRoleByIdReturnsOk() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNoteResponseBody responseBody = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.EDITOR);
        when(userNoteService.updateUserNoteRoleById(id, UserNoteRole.EDITOR)).thenReturn(Mono.just(responseBody));

        webTestClient.patch().uri("/user-notes/{id}", id).contentType(MediaType.APPLICATION_JSON).bodyValue(UserNoteRole.EDITOR).exchange()
            .expectStatus().isOk()
            .expectBody(UserNoteResponseBody.class).isEqualTo(responseBody);
    }

    @Test
    void deleteUserNoteByIdReturnsOk() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNoteResponseBody responseBody = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.EDITOR);
        when(userNoteService.deleteUserNoteById(id)).thenReturn(Mono.just(responseBody));

        webTestClient.delete().uri("/user-notes/{id}", id).exchange()
            .expectStatus().isOk()
            .expectBody(UserNoteResponseBody.class).isEqualTo(responseBody);
    }
}
