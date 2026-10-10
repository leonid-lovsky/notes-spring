package com.example.usernote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.Objects;
import java.util.UUID;

@SpringBootTest
@AutoConfigureRestTestClient
class UserNoteIntegrationTests {

    @Autowired
    private RestTestClient restTestClient;

    @Test
    void createReadUpdateDeleteFlow() {
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNoteRequestBody requestBody = new UserNoteRequestBody(userId, noteId, UserNoteRole.EDITOR);

        UserNoteResponseBody actualResponseBody = restTestClient.post().uri("/user-notes").body(requestBody).exchange()
            .expectStatus().isOk()
            .expectBody(UserNoteResponseBody.class).returnResult().getResponseBody();

        UserNoteResponseBody createdResponseBody = Objects.requireNonNull(actualResponseBody);
        UUID id = createdResponseBody.id();

        restTestClient.get().uri("/user-notes/{id}", id).exchange()
            .expectStatus().isOk()
            .expectBody(UserNoteResponseBody.class).isEqualTo(createdResponseBody);

        UserNoteResponseBody expected = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.VIEWER);

        restTestClient.patch().uri("/user-notes/{id}", id).body(UserNoteRole.VIEWER).exchange()
            .expectStatus().isOk()
            .expectBody(UserNoteResponseBody.class).isEqualTo(expected);

        restTestClient.delete().uri("/user-notes/{id}", id).exchange()
            .expectStatus().isOk()
            .expectBody(UserNoteResponseBody.class).isEqualTo(expected);

        restTestClient.get().uri("/user-notes/{id}", id).exchange()
            .expectStatus().isNotFound();
    }
}
