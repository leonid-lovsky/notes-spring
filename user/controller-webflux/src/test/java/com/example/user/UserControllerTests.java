package com.example.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;

@WebFluxTest(UserController.class)
class UserControllerTests {

    @Test
    void contextLoads() {
        /* context startup is the assertion */
    }
}
