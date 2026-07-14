package com.example.note.data.jpa.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.jspecify.annotations.NullUnmarked;
import org.jspecify.annotations.Nullable;

@NullUnmarked
@Entity
@Table(name = "notes")
public class NoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private @Nullable UUID id;

    @Column(nullable = false)
    private String content;

    protected NoteEntity() {

    }

    public NoteEntity(String content) {
        this.content = content;
    }

    public NoteEntity(UUID id, String content) {
        this.id = id;
        this.content = content;
    }

    public @Nullable UUID getId() {
        return this.id;
    }

    public String getContent() {
        return this.content;
    }
}
