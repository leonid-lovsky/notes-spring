package com.example.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

@WebMvcTest(UserController.class)
class UserControllerTests {

    @Test
    void contextLoads() {
        /* context startup is the assertion */
    }
}
