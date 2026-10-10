package com.example.usernote;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.UUID;
import java.util.stream.Stream;

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

        UserNoteResponseBody responseBody = new UserNoteResponseBody(id, UUID.randomUUID(), UUID.randomUUID(), UserNoteRole.EDITOR);
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
    void findUserNoteByIdWithInvalidIdReturnsBadRequest() {
        restTestClient.get().uri("/user-notes/{id}", "not-a-uuid").exchange()
            .expectStatus().isBadRequest();

        verifyNoInteractions(userNoteService);
    }

    @Test
    void createUserNoteReturnsOk() {
        UserNoteRequestBody requestBody = new UserNoteRequestBody(UUID.randomUUID(), UUID.randomUUID(), UserNoteRole.EDITOR);
        UserNoteResponseBody responseBody = new UserNoteResponseBody(UUID.randomUUID(), requestBody.userId(), requestBody.noteId(), UserNoteRole.EDITOR);

        when(userNoteService.createUserNote(requestBody)).thenReturn(responseBody);

        restTestClient.post().uri("/user-notes").contentType(MediaType.APPLICATION_JSON).body(requestBody).exchange()
            .expectStatus().isOk()
            .expectBody(UserNoteResponseBody.class).isEqualTo(responseBody);
    }

    @ParameterizedTest
    @MethodSource("requestBodiesWithMissingField")
    void createUserNoteWithMissingFieldReturnsBadRequest(UserNoteRequestBody requestBody) {
        restTestClient.post().uri("/user-notes").contentType(MediaType.APPLICATION_JSON).body(requestBody).exchange()
            .expectStatus().isBadRequest();

        verifyNoInteractions(userNoteService);
    }

    static Stream<UserNoteRequestBody> requestBodiesWithMissingField() {
        return Stream.of(
            new UserNoteRequestBody(null, UUID.randomUUID(), UserNoteRole.EDITOR),
            new UserNoteRequestBody(UUID.randomUUID(), null, UserNoteRole.EDITOR),
            new UserNoteRequestBody(UUID.randomUUID(), UUID.randomUUID(), null)
        );
    }

    @Test
    void updateUserNoteRoleByIdReturnsOk() {
        UUID id = UUID.randomUUID();

        UserNoteResponseBody responseBody = new UserNoteResponseBody(id, UUID.randomUUID(), UUID.randomUUID(), UserNoteRole.VIEWER);
        when(userNoteService.updateUserNoteRoleById(id, UserNoteRole.VIEWER)).thenReturn(responseBody);

        restTestClient.patch().uri("/user-notes/{id}", id).contentType(MediaType.APPLICATION_JSON).body(UserNoteRole.VIEWER).exchange()
            .expectStatus().isOk()
            .expectBody(UserNoteResponseBody.class).isEqualTo(responseBody);
    }

    @Test
    void updateUserNoteRoleByIdReturnsNotFound() {
        UUID id = UUID.randomUUID();

        when(userNoteService.updateUserNoteRoleById(id, UserNoteRole.VIEWER)).thenThrow(new UserNoteNotFoundException(id));

        restTestClient.patch().uri("/user-notes/{id}", id).contentType(MediaType.APPLICATION_JSON).body(UserNoteRole.VIEWER).exchange()
            .expectStatus().isNotFound();
    }

    @Test
    void updateUserNoteRoleByIdWithUnknownRoleReturnsBadRequest() {
        UUID id = UUID.randomUUID();

        restTestClient.patch().uri("/user-notes/{id}", id).contentType(MediaType.APPLICATION_JSON).body(UnknownRole.UNKNOWN).exchange()
            .expectStatus().isBadRequest();

        verifyNoInteractions(userNoteService);
    }

    @Test
    void updateUserNoteRoleByIdWithoutBodyReturnsBadRequest() {
        UUID id = UUID.randomUUID();

        restTestClient.patch().uri("/user-notes/{id}", id).contentType(MediaType.APPLICATION_JSON).exchange()
            .expectStatus().isBadRequest();

        verifyNoInteractions(userNoteService);
    }

    @Test
    void deleteUserNoteByIdReturnsOk() {
        UUID id = UUID.randomUUID();

        UserNoteResponseBody responseBody = new UserNoteResponseBody(id, UUID.randomUUID(), UUID.randomUUID(), UserNoteRole.EDITOR);
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

    private enum UnknownRole {
        UNKNOWN
    }
}
