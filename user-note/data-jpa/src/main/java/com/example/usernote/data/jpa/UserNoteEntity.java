package com.example.usernote.data.jpa;

import com.example.usernote.domain.UserNote;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.UUID;

@Entity
@Table(name = "user_notes", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "note_id"}))
class UserNoteEntity {

    @Id
    UUID id;
    @Column(nullable = false)
    String userId;
    @Column(nullable = false)
    UUID noteId;
    @Column(nullable = false)
    String ownerId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    UserNote.Permission permission;

    protected UserNoteEntity() {}
}
