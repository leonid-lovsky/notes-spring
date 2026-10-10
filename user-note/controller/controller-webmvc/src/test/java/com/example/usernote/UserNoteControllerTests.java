package com.example.usernote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserNoteController.class)
class UserNoteControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserNoteService userNoteService;

    @Test
    void findUserNoteByIdReturnsOk() throws Exception {
        UUID id = UUID.randomUUID();

        UserNoteResponseBody responseBody = new UserNoteResponseBody(id, UUID.randomUUID(), UUID.randomUUID(), UserNoteRole.EDITOR);
        when(userNoteService.findUserNoteById(id)).thenReturn(responseBody);

        mockMvc.perform(get("/user-notes/{id}", id))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(responseBody)));
    }

    @Test
    void findUserNoteByIdReturnsNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        when(userNoteService.findUserNoteById(id)).thenThrow(new UserNoteNotFoundException(id));

        mockMvc.perform(get("/user-notes/{id}", id))
            .andExpect(status().isNotFound());
    }

    @Test
    void createUserNoteReturnsOk() throws Exception {
        UserNoteRequestBody requestBody = new UserNoteRequestBody(UUID.randomUUID(), UUID.randomUUID(), UserNoteRole.EDITOR);
        UserNoteResponseBody responseBody = new UserNoteResponseBody(UUID.randomUUID(), requestBody.userId(), requestBody.noteId(), UserNoteRole.EDITOR);

        when(userNoteService.createUserNote(requestBody)).thenReturn(responseBody);

        mockMvc.perform(post("/user-notes").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(requestBody)))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(responseBody)));
    }

    @Test
    void createUserNoteWithoutUserIdReturnsBadRequest() throws Exception {
        Map<String, Object> requestBody = Map.of("noteId", UUID.randomUUID(), "role", UserNoteRole.EDITOR);

        mockMvc.perform(post("/user-notes").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(requestBody)))
            .andExpect(status().isBadRequest());

        verifyNoInteractions(userNoteService);
    }

    @Test
    void updateUserNoteRoleByIdReturnsOk() throws Exception {
        UUID id = UUID.randomUUID();

        UserNoteResponseBody responseBody = new UserNoteResponseBody(id, UUID.randomUUID(), UUID.randomUUID(), UserNoteRole.VIEWER);
        when(userNoteService.updateUserNoteRoleById(id, UserNoteRole.VIEWER)).thenReturn(responseBody);

        mockMvc.perform(patch("/user-notes/{id}", id).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(UserNoteRole.VIEWER)))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(responseBody)));
    }

    @Test
    void deleteUserNoteByIdReturnsOk() throws Exception {
        UUID id = UUID.randomUUID();

        UserNoteResponseBody responseBody = new UserNoteResponseBody(id, UUID.randomUUID(), UUID.randomUUID(), UserNoteRole.EDITOR);
        when(userNoteService.deleteUserNoteById(id)).thenReturn(responseBody);

        mockMvc.perform(delete("/user-notes/{id}", id))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(responseBody)));
    }
}
