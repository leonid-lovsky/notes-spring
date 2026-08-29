package com.example.usernote;

import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.bson.Document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Import(UserNoteTestConfiguration.class)
class UserNoteApplicationTests {

    @Nested
    @ActiveProfiles("mongodb")
    class MongoDB {

        @Autowired
        MongoTemplate mongoTemplate;

        @Test
        void contextLoads() {
            Assertions
                .assertThat(this.mongoTemplate.executeCommand(new Document("buildInfo", 1)))
                .containsKey("version");
        }
    }
}
