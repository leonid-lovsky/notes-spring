package com.example.usernote.domain;

import java.util.UUID;

public record UserNote(
    UUID id,
    String userId,
    UUID noteId,
    String ownerId,
    Permission permission
) {
    public static UserNote create(String userId, UUID noteId, String ownerId, Permission permission) {
        return new UserNote(UUID.randomUUID(), userId, noteId, ownerId, permission);
    }

    public enum Permission { READ, WRITE }
}
