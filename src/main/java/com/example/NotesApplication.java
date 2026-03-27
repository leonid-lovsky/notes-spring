package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.modulith.Modulithic;

@Modulithic
@SpringBootApplication
class NotesApplication {

    // TODO: https://docs.spring.io/spring-modulith/reference/verification.html
    // TODO: https://docs.spring.io/spring-modulith/reference/testing.html
    public static void main(String... args) {
        SpringApplication.run(NotesApplication.class, args);
    }
}
