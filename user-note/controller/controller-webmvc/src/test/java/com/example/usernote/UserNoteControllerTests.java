package com.example.usernote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.verifyNoInteractions;
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

        when(userNoteService.findUserNoteById(id)).thenThrow(new UserNoteNotFoundException(id));

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

        restTestClient.post().uri("/user-notes").contentType(MediaType.APPLICATION_JSON).body(requestBody).exchange()
            .expectStatus().isOk()
            .expectBody(UserNoteResponseBody.class).isEqualTo(responseBody);
    }

    @Test
    void createUserNoteWithoutUserIdReturnsBadRequest() {
        UUID noteId = UUID.randomUUID();

        Map<String, Object> requestBody = Map.of("noteId", noteId, "role", UserNoteRole.EDITOR);

        restTestClient.post().uri("/user-notes").contentType(MediaType.APPLICATION_JSON).body(requestBody).exchange()
            .expectStatus().isBadRequest();

        verifyNoInteractions(userNoteService);
    }

    @Test
    void updateUserNoteRoleByIdReturnsOk() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNoteResponseBody responseBody = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.EDITOR);

        when(userNoteService.updateUserNoteRoleById(id, UserNoteRole.EDITOR)).thenReturn(responseBody);

        restTestClient.patch().uri("/user-notes/{id}", id).contentType(MediaType.APPLICATION_JSON).body(UserNoteRole.EDITOR).exchange()
            .expectStatus().isOk()
            .expectBody(UserNoteResponseBody.class).isEqualTo(responseBody);
    }

    @Test
    void updateUserNoteRoleByIdReturnsNotFound() {
        UUID id = UUID.randomUUID();

        when(userNoteService.updateUserNoteRoleById(id, UserNoteRole.EDITOR)).thenThrow(new UserNoteNotFoundException(id));

        restTestClient.patch().uri("/user-notes/{id}", id).contentType(MediaType.APPLICATION_JSON).body(UserNoteRole.EDITOR).exchange()
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

        when(userNoteService.deleteUserNoteById(id)).thenThrow(new UserNoteNotFoundException(id));

        restTestClient.delete().uri("/user-notes/{id}", id).exchange()
            .expectStatus().isNotFound();
    }
}
