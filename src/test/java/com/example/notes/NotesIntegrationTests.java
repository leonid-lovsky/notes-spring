package com.example.notes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Notes Integration Tests")
class NotesIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NotesRepository repository;

    @Autowired
    private NotesProperties properties;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        assertThat(repository.count()).isZero();
    }

    @Test
    @DisplayName("GET /greeting returns Hello, World")
    void greetingReturnsHelloWorld() throws Exception {
        mockMvc.perform(get(properties.baseUrl() + "/greeting"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(properties.messages().helloWorld()));
    }

    @Test
    @DisplayName("POST / creates note successfully")
    void createNote() throws Exception {
        String payload = "{ \"content\": \"Hello, World\" }";

        mockMvc.perform(post(properties.baseUrl())
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.content").value("Hello, World"));

        NotesEntity saved = repository.findAll().get(0);
        assertThat(saved.getContent()).isEqualTo("Hello, World");
    }

    @Test
    @DisplayName("POST / fails with empty payload")
    void createNoteFailsOnEmptyPayload() throws Exception {
        String payload = "{}";

        mockMvc.perform(post(properties.baseUrl())
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    @DisplayName("GET /{id} returns note")
    void getNoteById() throws Exception {
        NotesEntity entity = repository.save(NotesEntity.builder().content("Hello").build());

        mockMvc.perform(get(properties.baseUrl() + "/{id}", entity.getId()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(entity.getId().toString()))
            .andExpect(jsonPath("$.content").value("Hello"));
    }

    @Test
    @DisplayName("GET /{id} returns 404 when note not exists")
    void getNoteByIdNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();

        mockMvc.perform(get(properties.baseUrl() + "/{id}", randomId))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    @DisplayName("GET / returns all application")
    void getAllNotes() throws Exception {
        repository.save(NotesEntity.builder().content("A").build());
        repository.save(NotesEntity.builder().content("B").build());

        mockMvc.perform(get(properties.baseUrl()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()", is(2)));
    }

    @Test
    @DisplayName("PUT /{id} updates note successfully")
    void updateNote() throws Exception {
        NotesEntity entity = repository.save(NotesEntity.builder().content("Old").build());
        String payload = "{ \"content\": \"Updated\" }";

        mockMvc.perform(put(properties.baseUrl() + "/{id}", entity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").value("Updated"));

        NotesEntity updated = repository.findById(entity.getId()).orElseThrow();
        assertThat(updated.getContent()).isEqualTo("Updated");
    }

    @Test
    @DisplayName("PUT /{id} returns 404 when note not exists")
    void updateNoteNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        String payload = "{ \"content\": \"Updated\" }";

        mockMvc.perform(put(properties.baseUrl() + "/{id}", randomId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    @DisplayName("DELETE /{id} removes note")
    void deleteNote() throws Exception {
        NotesEntity entity = repository.save(NotesEntity.builder().content("To delete").build());

        mockMvc.perform(delete(properties.baseUrl() + "/{id}", entity.getId()))
            .andDo(print())
            .andExpect(status().isNoContent());

        assertThat(repository.existsById(entity.getId())).isFalse();
    }

    @Test
    @DisplayName("DELETE /{id} returns 404 when note not exists")
    void deleteNoteNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();

        mockMvc.perform(delete(properties.baseUrl() + "/{id}", randomId))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
    }
}