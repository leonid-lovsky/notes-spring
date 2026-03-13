package com.example.note;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity(name = "note")
class NoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String content;

    public NoteEntity() {
    }

    UUID getId() {
        return id;
    }

    String getContent() {
        return content;
    }

    NoteEntity setContent(String content) {
        this.content = content;
        return this;
    }
}
