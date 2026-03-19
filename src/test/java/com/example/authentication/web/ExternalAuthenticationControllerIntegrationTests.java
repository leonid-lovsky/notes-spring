package com.example.authentication.web;

import com.example.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@DisplayName("External Authentication Controller Integration Tests")
class ExternalAuthenticationControllerIntegrationTests extends IntegrationTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Link external authentication method")
    void linkExternalAuthenticationMethod() throws Exception {
        registerUser(mockMvc, "alice", "Alice", "password123");

        mockMvc.perform(post("/api/authentication/methods")
                .with(httpBasic("alice", "password123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "provider": "GITHUB",
                      "externalUserId": "github-1",
                      "externalUsername": "alice-github"
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.provider").value("GITHUB"))
            .andExpect(jsonPath("$.externalUserId").value("github-1"))
            .andExpect(jsonPath("$.externalUsername").value("alice-github"));

        mockMvc.perform(get("/api/authentication/methods")
                .with(httpBasic("alice", "password123")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].provider").value("GITHUB"));
    }

    @Test
    @DisplayName("Reject duplicate provider for current user")
    void rejectDuplicateProviderForCurrentUser() throws Exception {
        registerUser(mockMvc, "alice", "Alice", "password123");

        mockMvc.perform(post("/api/authentication/methods")
                .with(httpBasic("alice", "password123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "provider": "GOOGLE",
                      "externalUserId": "google-1",
                      "externalUsername": "alice-google"
                    }
                    """))
            .andExpect(status().isCreated());

        mockMvc.perform(post("/api/authentication/methods")
                .with(httpBasic("alice", "password123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "provider": "GOOGLE",
                      "externalUserId": "google-2",
                      "externalUsername": "alice-google-2"
                    }
                    """))
            .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Reject duplicate external account across users")
    void rejectDuplicateExternalAccountAcrossUsers() throws Exception {
        registerUser(mockMvc, "alice", "Alice", "password123");
        registerUser(mockMvc, "bob", "Bob", "password123");

        mockMvc.perform(post("/api/authentication/methods")
                .with(httpBasic("alice", "password123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "provider": "TELEGRAM",
                      "externalUserId": "telegram-1",
                      "externalUsername": "alice-telegram"
                    }
                    """))
            .andExpect(status().isCreated());

        mockMvc.perform(post("/api/authentication/methods")
                .with(httpBasic("bob", "password123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "provider": "TELEGRAM",
                      "externalUserId": "telegram-1",
                      "externalUsername": "bob-telegram"
                    }
                    """))
            .andExpect(status().isConflict());
    }
}
