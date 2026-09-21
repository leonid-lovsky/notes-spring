package com.example.usernote;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;

@DataMongoTest
class UserNoteRepositoryTests {

    @SpringBootApplication
    static class SliceApplication {}

    @Test
    void contextLoads() {}
}
