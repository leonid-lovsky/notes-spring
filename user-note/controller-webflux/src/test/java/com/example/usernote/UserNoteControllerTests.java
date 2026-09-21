package com.example.usernote;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;

@WebFluxTest
class UserNoteControllerTests {

    @SpringBootApplication
    static class SliceApplication {}

    @Test
    void contextLoads() {}
}
