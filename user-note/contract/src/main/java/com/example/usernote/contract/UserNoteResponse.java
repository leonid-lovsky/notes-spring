package com.example.usernote.contract;

import java.util.UUID;

public record UserNoteResponse(
    UUID id,
    UUID userId,
    UUID noteId
) {
}
