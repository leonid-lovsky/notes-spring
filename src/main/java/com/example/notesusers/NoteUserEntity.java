package com.example.notesusers;

import com.example.note.NoteEntity;
import com.example.user.UserEntity;
import jakarta.persistence.*;

import java.util.UUID;

@Entity(name = "note_user")
class NoteUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private NoteEntity note;

    @Column(nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NoteUserRole role;
}
