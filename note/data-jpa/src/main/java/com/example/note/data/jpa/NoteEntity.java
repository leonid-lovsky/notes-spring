package com.example.note.data.jpa;

import jakarta.persistence.*;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

@Entity
@Table(name = "notes")
class NoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private @Nullable UUID id;

    @Column(nullable = false)
    private String content;

    @SuppressWarnings("NullAway.Init")
    protected NoteEntity() {

    }

    NoteEntity(String content) {
        this.content = content;
    }

    NoteEntity(UUID id, String content) {
        this.id = id;
        this.content = content;
    }

    @Nullable UUID getId() {
        return id;
    }

    String getContent() {
        return content;
    }

}
