package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.modulith.Modulithic;

@Modulithic
@SpringBootApplication
class NotesApplication {

    public static void main(String... args) {
        SpringApplication.run(NotesApplication.class, args);
    }
}
