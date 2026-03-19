package com.example.user.web;

import com.example.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@DisplayName("User Controller Integration Tests")
class UserControllerIntegrationTests extends IntegrationTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Register user")
    void registerUser() throws Exception {
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "username": "alice",
                      "displayName": "Alice",
                      "password": "password123"
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.username").value("alice"))
            .andExpect(jsonPath("$.displayName").value("Alice"));

        assertThat(userRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Reject duplicate username")
    void rejectDuplicateUsername() throws Exception {
        registerUser(mockMvc, "alice", "Alice", "password123");

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "username": "alice",
                      "displayName": "Alice Again",
                      "password": "password123"
                    }
                    """))
            .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Return current user profile")
    void returnCurrentUserProfile() throws Exception {
        registerUser(mockMvc, "alice", "Alice", "password123");

        mockMvc.perform(get("/api/users/me")
                .with(httpBasic("alice", "password123")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("alice"))
            .andExpect(jsonPath("$.displayName").value("Alice"));
    }
}
