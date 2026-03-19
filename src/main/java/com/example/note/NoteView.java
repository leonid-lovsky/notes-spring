package com.example.note;

import java.time.Instant;
import java.util.UUID;

public record NoteView(
    UUID id,
    String title,
    String content,
    Instant createdAt,
    Instant updatedAt
) {

}
