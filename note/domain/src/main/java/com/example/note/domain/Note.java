package com.example.note.domain;

import java.time.Instant;
import java.util.UUID;

public record Note(
    UUID id,
    String ownerId,
    String title,
    String content,
    Instant createdAt
) {
    public static Note create(String ownerId, String title, String content) {
        return new Note(UUID.randomUUID(), ownerId, title, content, Instant.now());
    }

    public Note update(String title, String content) {
        return new Note(id, ownerId, title, content, createdAt);
    }
}
