package com.example.note.domain;

import java.util.UUID;

public record NoteResponse(UUID id, String content) {
}
