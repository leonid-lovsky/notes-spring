package com.example.usernote.data.jpa;

import com.example.usernote.domain.UserNoteRole;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_notes")
class UserNoteEntity {

    @EmbeddedId
    private UserNoteId id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserNoteRole role;

    @SuppressWarnings("NullAway.Init")
    protected UserNoteEntity() {

    }

    UserNoteEntity(UserNoteId id, UserNoteRole role) {
        this.id = id;
        this.role = role;
    }

    UserNoteId getId() {
        return this.id;
    }

    UserNoteRole getRole() {
        return this.role;
    }

}
