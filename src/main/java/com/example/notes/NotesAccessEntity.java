package com.example.notes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Entity(name = "notes_access")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class NotesAccessEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID noteId;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String role;
}
