package com.example.usernote;

import java.util.UUID;

public record UserNote(UUID id, UUID userId, UUID noteId) {}
