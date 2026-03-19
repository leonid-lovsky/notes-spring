package com.example.noteuser.persistence;

import com.example.noteuser.NoteAccessRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(
    name = "note_access",
    uniqueConstraints = @UniqueConstraint(name = "uk_note_access_note_user", columnNames = {"note_id", "user_id"})
)
@Getter
@NoArgsConstructor
public class NoteAccessEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Setter
    @Column(nullable = false)
    private UUID noteId;

    @Setter
    @Column(nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Setter
    @Column(nullable = false)
    private NoteAccessRole role;
}
