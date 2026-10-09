package com.example.usernote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@WebMvcTest(UserNoteController.class)
class UserNoteControllerTests {

    @Autowired
    private MockMvcTester mockMvcTester;

    @MockitoBean
    private UserNoteService userNoteService;

    @Test
    void findUserNoteByIdReturnsOk() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNoteResponseBody responseBody = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.EDITOR);
        when(userNoteService.findUserNoteById(id)).thenReturn(responseBody);
        MvcTestResult result = mockMvcTester.get().uri("/user-notes/{id}", id).exchange();

        String expected = "{\"id\":\"%s\",\"userId\":\"%s\",\"noteId\":\"%s\",\"role\":\"EDITOR\"}".formatted(id, userId, noteId);

        assertThat(result).hasStatusOk();
        assertThat(result).bodyText().isEqualTo(expected);
    }

    @Test
    void createUserNoteReturnsOk() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNoteRequestBody requestBody = new UserNoteRequestBody(userId, noteId, UserNoteRole.EDITOR);
        UserNoteResponseBody responseBody = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.EDITOR);
        String content = "{\"userId\":\"%s\",\"noteId\":\"%s\",\"role\":\"EDITOR\"}".formatted(userId, noteId);
        when(userNoteService.createUserNote(requestBody)).thenReturn(responseBody);
        MvcTestResult result = mockMvcTester.post().uri("/user-notes").contentType(MediaType.APPLICATION_JSON).content(content).exchange();

        String expected = "{\"id\":\"%s\",\"userId\":\"%s\",\"noteId\":\"%s\",\"role\":\"EDITOR\"}".formatted(id, userId, noteId);

        assertThat(result).hasStatusOk();
        assertThat(result).bodyText().isEqualTo(expected);
    }

    @Test
    void createUserNoteWithoutUserIdReturnsBadRequest() {
        UUID noteId = UUID.randomUUID();

        String content = "{\"noteId\":\"%s\",\"role\":\"EDITOR\"}".formatted(noteId);
        MvcTestResult result = mockMvcTester.post().uri("/user-notes").contentType(MediaType.APPLICATION_JSON).content(content).exchange();

        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST);

        verifyNoInteractions(userNoteService);
    }

    @Test
    void updateUserNoteRoleByIdReturnsOk() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNoteResponseBody responseBody = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.EDITOR);
        when(userNoteService.updateUserNoteRoleById(id, UserNoteRole.EDITOR)).thenReturn(responseBody);
        MvcTestResult result = mockMvcTester.patch().uri("/user-notes/{id}", id).contentType(MediaType.APPLICATION_JSON).content("\"EDITOR\"").exchange();

        String expected = "{\"id\":\"%s\",\"userId\":\"%s\",\"noteId\":\"%s\",\"role\":\"EDITOR\"}".formatted(id, userId, noteId);

        assertThat(result).hasStatusOk();
        assertThat(result).bodyText().isEqualTo(expected);
    }

    @Test
    void deleteUserNoteByIdReturnsOk() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNoteResponseBody responseBody = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.EDITOR);
        when(userNoteService.deleteUserNoteById(id)).thenReturn(responseBody);
        MvcTestResult result = mockMvcTester.delete().uri("/user-notes/{id}", id).exchange();

        String expected = "{\"id\":\"%s\",\"userId\":\"%s\",\"noteId\":\"%s\",\"role\":\"EDITOR\"}".formatted(id, userId, noteId);

        assertThat(result).hasStatusOk();
        assertThat(result).bodyText().isEqualTo(expected);
    }
}
