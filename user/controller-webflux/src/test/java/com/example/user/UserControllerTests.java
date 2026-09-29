package com.example.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebFluxTest(UserController.class)
@MockitoBean(types = {FindUserByIdReactive.class, CreateUserByIdReactive.class, ReplaceUserByIdReactive.class, UpdateUserByIdReactive.class, DeleteUserByIdReactive.class})
class UserControllerTests {

    @Test
    void contextLoads() {
        /* context startup is the assertion */
    }
}
