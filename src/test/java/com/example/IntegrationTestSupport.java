package com.example;

import com.example.note.persistence.NoteRepository;
import com.example.noteuser.persistence.NoteAccessRepository;
import com.example.user.persistence.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public abstract class IntegrationTestSupport {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected NoteRepository noteRepository;

    @Autowired
    protected NoteAccessRepository noteAccessRepository;

    @AfterEach
    void cleanUp() {
        noteAccessRepository.deleteAll();
        noteRepository.deleteAll();
        userRepository.deleteAll();
    }

    protected void registerUser(MockMvc mockMvc, String username, String displayName, String password) throws Exception {
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "username": "%s",
                      "displayName": "%s",
                      "password": "%s"
                    }
                    """.formatted(username, displayName, password)))
            .andExpect(status().isCreated());
    }

    protected String readTextField(String json, String fieldName) throws Exception {
        String pattern = ".*\\\"" + fieldName + "\\\":\\\"([^\\\"]+)\\\".*";
        return json.replaceAll(pattern, "$1");
    }
}
