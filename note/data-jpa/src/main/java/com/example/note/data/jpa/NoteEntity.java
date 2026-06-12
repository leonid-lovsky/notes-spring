package com.example.note.data.jpa;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "notes")
class NoteEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String content;

    protected NoteEntity() {

    }

    NoteEntity(UUID id, String content) {
        this.id = id;
        this.content = content;
    }

    UUID getId() {
        return id;
    }

    String getContent() {
        return content;
    }
}
