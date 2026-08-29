package com.example.usernote;

import org.springframework.boot.SpringApplication;

public class TestUserNoteApplication {

    public static void main(String[] args) {
        SpringApplication
            .from(UserNoteApplication::main)
            .with(TestUserNoteConfiguration.class)
            .run(args);
    }
}
