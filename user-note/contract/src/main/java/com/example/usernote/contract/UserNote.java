package com.example.usernote.contract;

import java.util.UUID;

public record UserNote(
    UUID id,
    UUID userId,
    UUID noteId
) {
}
