package com.example.usernote.domain;

import java.util.UUID;

public record UserNoteRequest(UUID userId, UUID noteId, UserNoteRole role) {

}
