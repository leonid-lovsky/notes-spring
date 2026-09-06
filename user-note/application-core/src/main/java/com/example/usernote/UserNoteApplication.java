package com.example.usernote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public final class UserNoteApplication {

    private UserNoteApplication() {}

    public static void main(String[] args) {
        SpringApplication.run(UserNoteApplication.class, args);
    }
}
