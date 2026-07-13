package com.example.usernote.data.jpa.model;

import java.util.UUID;

import com.example.usernote.domain.UserNoteRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.jspecify.annotations.Nullable;

@Entity
@Table(name = "user_notes", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "note_id" }))
public class UserNoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private @Nullable UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "note_id", nullable = false)
    private UUID noteId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserNoteRole role;

    @SuppressWarnings("NullAway.Init")
    protected UserNoteEntity() {

    }

    public UserNoteEntity(UUID userId, UUID noteId, UserNoteRole role) {
        this.userId = userId;
        this.noteId = noteId;
        this.role = role;
    }

    public UserNoteEntity(UUID id, UUID userId, UUID noteId, UserNoteRole role) {
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

    public UserNoteRole getRole() {
        return this.role;
    }
}
