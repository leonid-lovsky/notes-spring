package com.example.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(UserController.class)
@MockitoBean(types = {FindUserById.class, CreateUserById.class, ReplaceUserById.class, UpdateUserById.class, DeleteUserById.class})
class UserControllerTests {

    @Test
    void contextLoads() {
        /* context startup is the assertion */
    }
}
