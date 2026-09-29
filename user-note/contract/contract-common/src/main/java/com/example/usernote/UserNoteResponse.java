package com.example.usernote;

import java.util.UUID;

public record UserNoteResponse(UUID id, UUID userId, UUID noteId) {}
