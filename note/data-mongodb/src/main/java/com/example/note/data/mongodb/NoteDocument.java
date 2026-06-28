package com.example.note.data.mongodb;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notes")
class NoteDocument {

    @Id
    private UUID id;

    private String content;

    @SuppressWarnings("NullAway.Init")
    protected NoteDocument() {

    }

    NoteDocument(UUID id, String content) {
        this.id = id;
        this.content = content;
    }

    UUID getId() {
        return this.id;
    }

    String getContent() {
        return this.content;
    }

}
