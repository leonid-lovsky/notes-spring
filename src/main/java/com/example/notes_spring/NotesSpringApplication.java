package com.example.notes_spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.modulith.Modulithic;

@Modulithic
@SpringBootApplication
public class NotesSpringApplication {

    public static void main(String... args) {
        SpringApplication.run(NotesSpringApplication.class, args);
    }
}
