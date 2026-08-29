package com.example.usernote;

import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.bson.Document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Import(UserNoteTestConfiguration.class)
class UserNoteApplicationTests {

    abstract static class ServerInfoTests {

        @Autowired
        ReactiveMongoTemplate reactiveMongoTemplate;

        @Test
        void contextLoads() {
            Assertions
                .assertThat(this.reactiveMongoTemplate.executeCommand(new Document("buildInfo", 1)).block())
                .containsKey("version");
        }
    }

    @Nested
    @ActiveProfiles("mongodb")
    class MongoDB extends ServerInfoTests {}
}
