package com.example.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebFluxTest(ReactiveUserController.class)
@MockitoBean(types = {ReactiveFindUserById.class, ReactiveCreateUserById.class, ReactiveReplaceUserById.class, ReactiveUpdateUserById.class, ReactiveDeleteUserById.class})
class ReactiveUserControllerTests {

    @Test
    void contextLoads() {
        /* context startup is the assertion */
    }
}
