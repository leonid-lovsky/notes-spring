package com.example.usernote;

import java.util.UUID;

public record UserNoteRequest(UUID userId, UUID noteId) {}
