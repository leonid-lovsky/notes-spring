package com.example.notesusers;

import com.example.note.NoteEntity;
import com.example.user.UserEntity;
import jakarta.persistence.*;

import java.util.UUID;

@Entity(name = "note_user")
@Table(name = "note_user")
public class NoteUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "note_id", nullable = false)
    private NoteEntity note;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private NoteUserRole role;
}
