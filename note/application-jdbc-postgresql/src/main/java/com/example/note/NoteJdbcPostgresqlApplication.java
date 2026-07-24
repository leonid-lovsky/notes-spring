package com.example.note;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NoteJdbcPostgresqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoteJdbcPostgresqlApplication.class, args);
    }
}
