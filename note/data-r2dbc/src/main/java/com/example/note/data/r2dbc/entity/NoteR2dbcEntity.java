package com.example.note.data.r2dbc.entity;

import java.util.UUID;

import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("notes")
public class NoteR2dbcEntity {

    @Id
    private @Nullable UUID id;

    @Column("content")
    private String content;

    @SuppressWarnings("NullAway.Init")
    protected NoteR2dbcEntity() {

    }

    public NoteR2dbcEntity(String content) {
        this.content = content;
    }

    public NoteR2dbcEntity(UUID id, String content) {
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
