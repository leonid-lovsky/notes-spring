package com.example.note.contract;

import java.util.UUID;

public record NoteEvent(
    NoteEventType type,
    UUID noteId,
    String content
) {
}
