package com.example.notes;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.notes")
record NotesProperties(
    String baseUrl,
    Messages messages
) {

    record Messages(
        String helloWorld,
        String noteNotFound,
        String cannotUpdate,
        String cannotDelete
    ) {

    }
}