package com.example.note;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Notes MockMvc Tests")
class NotesMockMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NoteRepository repository;

    @Value("${app.notes.base-url}")
    private String baseUrl;

    @Autowired
    private NoteMessages messages;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        assertThat(repository.count()).isZero();
    }

    @Test
    @DisplayName("GET /greeting returns Hello, World")
    void greetingReturnsHelloWorld() throws Exception {
        mockMvc.perform(get(baseUrl + "/greeting"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(messages.hello()));
    }

    @Test
    @DisplayName("POST / creates note")
    void postCreatesNote() throws Exception {
        String payload = "{ \"content\": \"Hello, World\" }";

        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.content").value("Hello, World"));

        NoteEntity saved = repository.findAll().get(0);
        assertThat(saved.getContent()).isEqualTo("Hello, World");
    }

    @Test
    @DisplayName("POST / fails with empty payload")
    void postFailsWithEmptyPayload() throws Exception {
        String payload = "";

        mockMvc.perform(post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    @DisplayName("GET /{id} returns note")
    void getByIdReturnsNote() throws Exception {
        NoteEntity entity = repository.save(new NoteEntity().setContent("Hello"));

        mockMvc.perform(get(baseUrl + "/{id}", entity.getId()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(entity.getId().toString()))
            .andExpect(jsonPath("$.content").value("Hello"));
    }

    @Test
    @DisplayName("GET /{id} returns 404 when note not exists")
    void getByIdReturnsNotFoundWhenNoteNotExists() throws Exception {
        UUID randomId = UUID.randomUUID();

        mockMvc.perform(get(baseUrl + "/{id}", randomId))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    @DisplayName("PUT /{id} updates note")
    void putByIdUpdatesNote() throws Exception {
        NoteEntity entity = repository.save(new NoteEntity().setContent("Old"));
        String payload = "{ \"content\": \"Updated\" }";

        mockMvc.perform(put(baseUrl + "/{id}", entity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").value("Updated"));

        NoteEntity updated = repository.findById(entity.getId()).orElseThrow();
        assertThat(updated.getContent()).isEqualTo("Updated");
    }

    @Test
    @DisplayName("PUT /{id} returns 404 when note not exists")
    void putByIdReturnsNotFoundWhenNoteNotExists() throws Exception {
        UUID randomId = UUID.randomUUID();
        String payload = "{ \"content\": \"Updated\" }";

        mockMvc.perform(put(baseUrl + "/{id}", randomId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    @DisplayName("DELETE /{id} removes note")
    void deleteByIdRemovesNote() throws Exception {
        NoteEntity entity = repository.save(new NoteEntity().setContent("To delete"));

        mockMvc.perform(delete(baseUrl + "/{id}", entity.getId()))
            .andDo(print())
            .andExpect(status().isNoContent());

        assertThat(repository.existsById(entity.getId())).isFalse();
    }

    @Test
    @DisplayName("DELETE /{id} returns 404 when note not exists")
    void deleteByIdReturnNotFoundWhenNoteNotExists() throws Exception {
        UUID randomId = UUID.randomUUID();

        mockMvc.perform(delete(baseUrl + "/{id}", randomId))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
    }
}
