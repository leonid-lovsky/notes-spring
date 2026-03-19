package com.example.note.web;

import com.example.IntegrationTestSupport;
import com.example.noteuser.NoteAccessRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@DisplayName("Note Controller Integration Tests")
class NoteControllerIntegrationTests extends IntegrationTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Reject unauthenticated requests")
    void rejectUnauthenticatedRequests() throws Exception {
        mockMvc.perform(get("/api/notes"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Create list read update replace and delete own note")
    void createListReadUpdateReplaceAndDeleteOwnNote() throws Exception {
        registerUser(mockMvc, "alice", "Alice", "password123");

        String createResponse = mockMvc.perform(post("/api/notes")
                .with(httpBasic("alice", "password123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "content": "My first note"
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.content").value("My first note"))
            .andReturn()
            .getResponse()
            .getContentAsString();

        String noteId = readTextField(createResponse, "id");

        mockMvc.perform(get("/api/notes")
                .with(httpBasic("alice", "password123")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(noteId))
            .andExpect(jsonPath("$[0].content").value("My first note"));

        mockMvc.perform(get("/api/notes/{id}", noteId)
                .with(httpBasic("alice", "password123")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(noteId))
            .andExpect(jsonPath("$.content").value("My first note"));

        mockMvc.perform(patch("/api/notes/{id}", noteId)
                .with(httpBasic("alice", "password123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "content": "Updated content"
                    }
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(noteId))
            .andExpect(jsonPath("$.content").value("Updated content"));

        mockMvc.perform(put("/api/notes/{id}", noteId)
                .with(httpBasic("alice", "password123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "content": "Replaced content"
                    }
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(noteId))
            .andExpect(jsonPath("$.content").value("Replaced content"));

        mockMvc.perform(delete("/api/notes/{id}", noteId)
                .with(httpBasic("alice", "password123")))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/notes")
                .with(httpBasic("alice", "password123")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isEmpty());

        assertThat(noteAccessRepository.count()).isZero();
    }

    @Test
    @DisplayName("Creator access is stored for new note")
    void creatorAccessIsStoredForNewNote() throws Exception {
        registerUser(mockMvc, "alice", "Alice", "password123");

        mockMvc.perform(post("/api/notes")
                .with(httpBasic("alice", "password123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "content": "My first note"
                    }
                    """))
            .andExpect(status().isCreated());

        assertThat(noteAccessRepository.findAll())
            .singleElement()
            .extracting(access -> access.getRole())
            .isEqualTo(NoteAccessRole.CREATOR);
    }

    @Test
    @DisplayName("Reject access to another user's note")
    void rejectAccessToAnotherUsersNote() throws Exception {
        registerUser(mockMvc, "alice", "Alice", "password123");
        registerUser(mockMvc, "bob", "Bob", "password123");

        String createResponse = mockMvc.perform(post("/api/notes")
                .with(httpBasic("alice", "password123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "content": "Only Alice can read this"
                    }
                    """))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        String noteId = readTextField(createResponse, "id");

        mockMvc.perform(get("/api/notes/{id}", noteId)
                .with(httpBasic("bob", "password123")))
            .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Reject invalid note request")
    void rejectInvalidNoteRequest() throws Exception {
        registerUser(mockMvc, "alice", "Alice", "password123");

        mockMvc.perform(post("/api/notes")
                .with(httpBasic("alice", "password123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "content": ""
                    }
                    """))
            .andExpect(status().isBadRequest());

        assertThat(noteRepository.count()).isZero();
    }

    @Test
    @DisplayName("Patch keeps note unchanged when request body does not include content")
    void patchKeepsNoteUnchangedWhenRequestBodyDoesNotIncludeContent() throws Exception {
        registerUser(mockMvc, "alice", "Alice", "password123");

        String createResponse = mockMvc.perform(post("/api/notes")
                .with(httpBasic("alice", "password123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "content": "Original content"
                    }
                    """))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        String noteId = readTextField(createResponse, "id");

        mockMvc.perform(patch("/api/notes/{id}", noteId)
                .with(httpBasic("alice", "password123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                    }
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").value("Original content"));
    }

    @Test
    @DisplayName("Reject invalid replace request")
    void rejectInvalidReplaceRequest() throws Exception {
        registerUser(mockMvc, "alice", "Alice", "password123");

        String createResponse = mockMvc.perform(post("/api/notes")
                .with(httpBasic("alice", "password123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "content": "Original content"
                    }
                    """))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        String noteId = readTextField(createResponse, "id");

        mockMvc.perform(put("/api/notes/{id}", noteId)
                .with(httpBasic("alice", "password123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "content": ""
                    }
                    """))
            .andExpect(status().isBadRequest());
    }
}
