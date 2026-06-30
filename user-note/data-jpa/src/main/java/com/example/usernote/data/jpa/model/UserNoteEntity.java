package com.example.usernote.data.jpa.model;

import com.example.usernote.domain.UserNoteRole;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_notes")
public class UserNoteEntity {

    @EmbeddedId
    private UserNoteId id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserNoteRole role;

    @SuppressWarnings("NullAway.Init")
    protected UserNoteEntity() {

    }

    public UserNoteEntity(UserNoteId id, UserNoteRole role) {
        this.id = id;
        this.role = role;
    }

    public UserNoteId getId() {
        return this.id;
    }

    public UserNoteRole getRole() {
        return this.role;
    }

}
