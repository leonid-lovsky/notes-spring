package com.example.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(UserTestConfiguration.class)
class UserApplicationTests {

    @Test
    void contextLoads() {}
}
