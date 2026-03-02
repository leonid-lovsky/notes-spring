package com.example.application.notes;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "app.notes")
class NotesProperties {

    private String baseUrl;
    private Messages messages = new Messages();

    @Setter
    @Getter
    static class Messages {

        private String helloWorld;
        private String noteNotFound;
        private String cannotUpdate;
        private String cannotDelete;
    }
}