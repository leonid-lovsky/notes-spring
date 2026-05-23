package com.example.usernote.data.jpa;

import com.example.usernote.domain.UserNote;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "user_notes")
class UserNoteEntity {

    @Id
    UUID id;
    String userId;
    UUID noteId;
    String ownerId;
    @Enumerated(EnumType.STRING)
    UserNote.Permission permission;

    protected UserNoteEntity() {}
}
