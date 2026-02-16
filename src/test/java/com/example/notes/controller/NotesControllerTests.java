package com.example.notes.controller;

import com.example.notes.service.NotesService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.mockito.Mockito.when;

@WebMvcTest(NotesController.class)
@AutoConfigureRestTestClient
class NotesControllerTests {

    // @Autowired
    // private NotesController controller;

    // @Test
    // void contextLoads() {
    //     assertThat(controller).isNotNull();
    // }

    @Autowired
    private RestTestClient restTestClient;

    @MockitoBean
    private NotesService service;

    @Test
    void greetingShouldReturnDefaultMessage() {
        when(service.greet()).thenReturn("Hello, World");
        restTestClient.get().uri("/")
            .exchange()
            .expectBody(String.class)
            .isEqualTo("Hello, World");
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void create() {
    }

    @Test
    void getById() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}
