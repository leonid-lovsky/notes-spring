package com.example.usernote.domain;

import java.util.UUID;

public record UserNoteResponse(UUID id, UUID userId, UUID noteId, UserNoteRole role) {
}
