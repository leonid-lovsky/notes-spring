package com.example.note;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NoteJdbcH2Application {

    public static void main(String[] args) {
        SpringApplication.run(NoteJdbcH2Application.class, args);
    }
}
