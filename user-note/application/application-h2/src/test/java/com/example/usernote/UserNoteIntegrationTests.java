package com.example.usernote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserNoteIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createReadUpdateDeleteFlow() throws Exception {
        UserNoteRequestBody requestBody = new UserNoteRequestBody(UUID.randomUUID(), UUID.randomUUID(), UserNoteRole.EDITOR);

        String createdJson = mockMvc.perform(post("/user-notes").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(requestBody)))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        UserNoteResponseBody created = objectMapper.readValue(createdJson, UserNoteResponseBody.class);

        mockMvc.perform(get("/user-notes/{id}", created.id()))
            .andExpect(status().isOk())
            .andExpect(content().json(createdJson));

        mockMvc.perform(patch("/user-notes/{id}", created.id()).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(UserNoteRole.VIEWER)))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(new UserNoteResponseBody(created.id(), created.userId(), created.noteId(), UserNoteRole.VIEWER))));

        mockMvc.perform(delete("/user-notes/{id}", created.id()))
            .andExpect(status().isOk());

        mockMvc.perform(get("/user-notes/{id}", created.id()))
            .andExpect(status().isNotFound());
    }
}
