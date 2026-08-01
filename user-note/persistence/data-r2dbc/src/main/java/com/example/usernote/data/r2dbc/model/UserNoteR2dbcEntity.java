package com.example.usernote.data.r2dbc.model;

import java.util.UUID;

import com.example.usernote.domain.UserNotePersistable;
import com.example.usernote.domain.UserNoteRole;
import org.jspecify.annotations.NullUnmarked;
import org.jspecify.annotations.Nullable;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@NullUnmarked
@Table("user_notes")
public class UserNoteR2dbcEntity implements UserNotePersistable {

    @Id
    private @Nullable UUID id;

    @Column("user_id")
    private UUID userId;

    @Column("note_id")
    private UUID noteId;

    @Column("role")
    private UserNoteRole role;

    protected UserNoteR2dbcEntity() {

    }

    public UserNoteR2dbcEntity(UUID userId, UUID noteId, UserNoteRole role) {
        this.userId = userId;
        this.noteId = noteId;
        this.role = role;
    }

    public UserNoteR2dbcEntity(UUID id, UUID userId, UUID noteId, UserNoteRole role) {
        this.id = id;
        this.userId = userId;
        this.noteId = noteId;
        this.role = role;
    }

    @Override
    public @Nullable UUID getId() {
        return this.id;
    }

    @Override
    public UUID getUserId() {
        return this.userId;
    }

    @Override
    public UUID getNoteId() {
        return this.noteId;
    }

    @Override
    public UserNoteRole getRole() {
        return this.role;
    }
}
