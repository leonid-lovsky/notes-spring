package com.example.usernote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;
import java.util.UUID;

@SpringBootTest
@AutoConfigureWebTestClient
class UserNoteIntegrationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void createReadUpdateDeleteFlow() {
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNoteRequestBody requestBody = new UserNoteRequestBody(userId, noteId, UserNoteRole.EDITOR);

        UserNoteResponseBody responseBody = webTestClient.post().uri("/user-notes").bodyValue(requestBody).exchange()
            .expectStatus().isOk()
            .expectBody(UserNoteResponseBody.class).returnResult().getResponseBody();

        UserNoteResponseBody createdResponseBody = Objects.requireNonNull(responseBody);

        UUID id = createdResponseBody.id();

        webTestClient.get().uri("/user-notes/{id}", id).exchange()
            .expectStatus().isOk()
            .expectBody(UserNoteResponseBody.class).isEqualTo(createdResponseBody);

        UserNoteResponseBody expected = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.VIEWER);

        webTestClient.patch().uri("/user-notes/{id}", id).bodyValue(UserNoteRole.VIEWER).exchange()
            .expectStatus().isOk()
            .expectBody(UserNoteResponseBody.class).isEqualTo(expected);

        webTestClient.delete().uri("/user-notes/{id}", id).exchange()
            .expectStatus().isOk()
            .expectBody(UserNoteResponseBody.class).isEqualTo(expected);

        webTestClient.get().uri("/user-notes/{id}", id).exchange()
            .expectStatus().isNotFound();
    }
}
