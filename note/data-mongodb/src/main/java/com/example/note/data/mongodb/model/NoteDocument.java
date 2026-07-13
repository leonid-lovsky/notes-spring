package com.example.note.data.mongodb.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notes")
public class NoteDocument {

    @Id
    private UUID id;

    private String content;

    @SuppressWarnings("NullAway.Init")
    protected NoteDocument() {

    }

    public NoteDocument(UUID id, String content) {
        this.id = id;
        this.content = content;
    }

    public UUID getId() {
        return this.id;
    }

    public String getContent() {
        return this.content;
    }
}
