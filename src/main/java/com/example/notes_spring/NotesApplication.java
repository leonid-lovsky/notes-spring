package com.example.notes_spring;

import com.example.notes_spring.configuration.NotesConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.modulith.Modulithic;

@Modulithic
@SpringBootApplication
@EnableConfigurationProperties(NotesConfigurationProperties.class)
public class NotesApplication {

    public static void main(String... args) {
        SpringApplication.run(NotesApplication.class, args);
    }
}
