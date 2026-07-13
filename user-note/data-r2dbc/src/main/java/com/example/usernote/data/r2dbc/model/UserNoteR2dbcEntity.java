package com.example.usernote.data.r2dbc.model;

import java.util.UUID;

import org.jspecify.annotations.Nullable;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("user_notes")
public class UserNoteR2dbcEntity {

    @Id
    private @Nullable UUID id;

    @Column("user_id")
    private UUID userId;

    @Column("note_id")
    private UUID noteId;

    @Column("role")
    private String role;

    @SuppressWarnings("NullAway.Init")
    protected UserNoteR2dbcEntity() {

    }

    public UserNoteR2dbcEntity(UUID userId, UUID noteId, String role) {
        this.userId = userId;
        this.noteId = noteId;
        this.role = role;
    }

    public UserNoteR2dbcEntity(UUID id, UUID userId, UUID noteId, String role) {
        this.id = id;
        this.userId = userId;
        this.noteId = noteId;
        this.role = role;
    }

    public @Nullable UUID getId() {
        return this.id;
    }

    public UUID getUserId() {
        return this.userId;
    }

    public UUID getNoteId() {
        return this.noteId;
    }

    public String getRole() {
        return this.role;
    }
}
