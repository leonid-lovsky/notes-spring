package com.example.notesusers;

import jakarta.persistence.*;

import java.util.UUID;

@Entity(name = "note_user")
class NoteUserEntity {

    @Id @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID noteId;

    @Column(nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NoteUserRole role;
}
