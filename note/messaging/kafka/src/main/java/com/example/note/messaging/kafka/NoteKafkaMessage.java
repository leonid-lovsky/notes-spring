package com.example.note.messaging.kafka;

import com.example.note.contract.NoteEventType;

import java.time.Instant;
import java.util.UUID;

public record NoteKafkaMessage(
    UUID outboxId,
    UUID noteId,
    NoteEventType type,
    String payload,
    Instant createdAt
) {
}
