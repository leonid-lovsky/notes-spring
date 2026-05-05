package com.example.note.contract;

import java.util.UUID;

public record NoteResponse(
    UUID id,
    String content
) {
}
