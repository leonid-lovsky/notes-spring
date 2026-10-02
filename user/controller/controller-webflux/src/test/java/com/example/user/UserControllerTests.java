package com.example.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebFluxTest(UserController.class)
@MockitoBean(types = {FindUserById.class, CreateUserById.class, ReplaceUserById.class, UpdateUserById.class, DeleteUserById.class})
class UserControllerTests {

    @Test
    void contextLoads() {
        /* context startup is the assertion */
    }
}
