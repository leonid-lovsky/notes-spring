package com.example.note.data.jpa;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.jspecify.annotations.Nullable;

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
        return this.id;
    }

    String getContent() {
        return this.content;
    }

}
