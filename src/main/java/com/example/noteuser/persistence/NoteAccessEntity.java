package com.example.noteuser.persistence;

import com.example.noteuser.NoteAccessRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
    name = "note_access",
    uniqueConstraints = @UniqueConstraint(name = "uk_note_access_note_user", columnNames = {"note_id", "user_id"})
)
@Getter
@Setter
@NoArgsConstructor
public class NoteAccessEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID noteId;

    @Column(nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NoteAccessRole role;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}
