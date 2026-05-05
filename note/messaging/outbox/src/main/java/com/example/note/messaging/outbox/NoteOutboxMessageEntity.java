package com.example.note.messaging.outbox;

import com.example.note.contract.NoteEventType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class NoteOutboxMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID noteId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NoteEventType type;

    @Column(nullable = false, length = 4000)
    private String payload;

    @Column(nullable = false)
    private Instant createdAt;
}
