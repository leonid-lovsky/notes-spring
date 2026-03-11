package com.example.notesusers;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Entity(name = "note_user")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class NoteUserEntity {

    @Id @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID noteId;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String role;
}
