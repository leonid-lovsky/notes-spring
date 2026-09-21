package com.example.usernote;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

@WebMvcTest
class UserNoteControllerTests {

    @SpringBootApplication
    static class SliceApplication {}

    @Test
    void contextLoads() {}
}
