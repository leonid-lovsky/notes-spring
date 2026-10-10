package com.example.usernote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
        UserNoteRequestBody requestBody = new UserNoteRequestBody(UUID.randomUUID(), UUID.randomUUID(), UserNoteRole.EDITOR);

        UserNoteResponseBody createResponse = restTestClient.post().uri("/user-notes").contentType(MediaType.APPLICATION_JSON).body(requestBody).exchange()
            .expectStatus().isOk()
            .expectBody(UserNoteResponseBody.class).returnResult().getResponseBody();

        UserNoteResponseBody created = Objects.requireNonNull(createResponse);
        UserNoteResponseBody updated = new UserNoteResponseBody(created.id(), created.userId(), created.noteId(), UserNoteRole.VIEWER);

        restTestClient.get().uri("/user-notes/{id}", created.id()).exchange()
            .expectStatus().isOk()
            .expectBody(UserNoteResponseBody.class).isEqualTo(created);

        restTestClient.patch().uri("/user-notes/{id}", created.id()).contentType(MediaType.APPLICATION_JSON).body(UserNoteRole.VIEWER).exchange()
            .expectStatus().isOk()
            .expectBody(UserNoteResponseBody.class).isEqualTo(updated);

        restTestClient.delete().uri("/user-notes/{id}", created.id()).exchange()
            .expectStatus().isOk()
            .expectBody(UserNoteResponseBody.class).isEqualTo(updated);

        restTestClient.get().uri("/user-notes/{id}", created.id()).exchange()
            .expectStatus().isNotFound();
    }
}
