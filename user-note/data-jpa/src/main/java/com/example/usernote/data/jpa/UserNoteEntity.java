package com.example.usernote.data.jpa;

import com.example.usernote.domain.UserNoteRole;
import jakarta.persistence.*;

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
        return id;
    }

    UserNoteRole getRole() {
        return role;
    }

}
