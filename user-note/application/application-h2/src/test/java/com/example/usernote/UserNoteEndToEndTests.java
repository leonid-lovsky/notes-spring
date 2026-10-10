package com.example.usernote;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(UserNoteTestConfiguration.class)
class UserNoteEndToEndTests {

    @LocalServerPort
    private int port;

    @Test
    void userNoteFlow() {
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        RestTestClient restTestClient = RestTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
        UserNoteRequestBody requestBody = new UserNoteRequestBody(userId, noteId, UserNoteRole.EDITOR);

        UserNoteResponseBody created = restTestClient.post().uri("/user-notes").contentType(MediaType.APPLICATION_JSON).body(requestBody).exchange()
            .expectStatus().isOk()
            .expectBody(UserNoteResponseBody.class).returnResult().getResponseBody();

        UserNoteResponseBody createdBody = Objects.requireNonNull(created);
        UserNoteResponseBody updatedBody = new UserNoteResponseBody(createdBody.id(), userId, noteId, UserNoteRole.VIEWER);

        assertNotNull(createdBody.id());

        restTestClient.get().uri("/user-notes/{id}", createdBody.id()).exchange()
            .expectStatus().isOk()
            .expectBody(UserNoteResponseBody.class).isEqualTo(createdBody);

        restTestClient.patch().uri("/user-notes/{id}", createdBody.id()).contentType(MediaType.APPLICATION_JSON).body(UserNoteRole.VIEWER).exchange()
            .expectStatus().isOk()
            .expectBody(UserNoteResponseBody.class).isEqualTo(updatedBody);

        restTestClient.get().uri("/user-notes/{id}", createdBody.id()).exchange()
            .expectStatus().isOk()
            .expectBody(UserNoteResponseBody.class).isEqualTo(updatedBody);

        restTestClient.delete().uri("/user-notes/{id}", createdBody.id()).exchange()
            .expectStatus().isOk()
            .expectBody(UserNoteResponseBody.class).isEqualTo(updatedBody);

        restTestClient.get().uri("/user-notes/{id}", createdBody.id()).exchange()
            .expectStatus().isNotFound();
    }
}
