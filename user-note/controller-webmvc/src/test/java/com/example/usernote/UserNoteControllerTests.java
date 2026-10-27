package com.example.usernote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.UUID;

import static org.mockito.Mockito.when;

@WebMvcTest(UserNoteController.class)
@AutoConfigureRestTestClient
class UserNoteControllerTests {

    @Autowired
    private RestTestClient restTestClient;

    @MockitoBean
    private UserNoteService userNoteService;

    @Test
    void findUserNoteByIdReturnsOk() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNoteResponseBody responseBody = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.EDITOR);

        when(userNoteService.findUserNoteById(id)).thenReturn(responseBody);

        restTestClient.get().uri("/user-notes/{id}", id).exchange()
            .expectStatus().isOk()
            .expectBody(UserNoteResponseBody.class).isEqualTo(responseBody);
    }

    @Test
    void findUserNoteByIdReturnsNotFound() {
        UUID id = UUID.randomUUID();

        UserNoteNotFoundException exception = new UserNoteNotFoundException(id);

        when(userNoteService.findUserNoteById(id)).thenThrow(exception);

        restTestClient.get().uri("/user-notes/{id}", id).exchange()
            .expectStatus().isNotFound();
    }

    @Test
    void createUserNoteReturnsOk() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNoteRequestBody requestBody = new UserNoteRequestBody(userId, noteId, UserNoteRole.EDITOR);
        UserNoteResponseBody responseBody = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.EDITOR);

        when(userNoteService.createUserNote(requestBody)).thenReturn(responseBody);

        restTestClient.post().uri("/user-notes").body(requestBody).exchange()
            .expectStatus().isOk()
            .expectBody(UserNoteResponseBody.class).isEqualTo(responseBody);
    }

    @Test
    void updateUserNoteRoleByIdReturnsOk() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNoteResponseBody responseBody = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.VIEWER);

        when(userNoteService.updateUserNoteRoleById(id, UserNoteRole.VIEWER)).thenReturn(responseBody);

        restTestClient.patch().uri("/user-notes/{id}", id).body(UserNoteRole.VIEWER).exchange()
            .expectStatus().isOk()
            .expectBody(UserNoteResponseBody.class).isEqualTo(responseBody);
    }

    @Test
    void updateUserNoteRoleByIdReturnsNotFound() {
        UUID id = UUID.randomUUID();

        UserNoteNotFoundException exception = new UserNoteNotFoundException(id);

        when(userNoteService.updateUserNoteRoleById(id, UserNoteRole.VIEWER)).thenThrow(exception);

        restTestClient.patch().uri("/user-notes/{id}", id).body(UserNoteRole.VIEWER).exchange()
            .expectStatus().isNotFound();
    }

    @Test
    void deleteUserNoteByIdReturnsOk() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNoteResponseBody responseBody = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.EDITOR);

        when(userNoteService.deleteUserNoteById(id)).thenReturn(responseBody);

        restTestClient.delete().uri("/user-notes/{id}", id).exchange()
            .expectStatus().isOk()
            .expectBody(UserNoteResponseBody.class).isEqualTo(responseBody);
    }

    @Test
    void deleteUserNoteByIdReturnsNotFound() {
        UUID id = UUID.randomUUID();

        UserNoteNotFoundException exception = new UserNoteNotFoundException(id);

        when(userNoteService.deleteUserNoteById(id)).thenThrow(exception);

        restTestClient.delete().uri("/user-notes/{id}", id).exchange()
            .expectStatus().isNotFound();
    }
}
