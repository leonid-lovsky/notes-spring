package com.example.user.domain;

import java.util.UUID;

public record UserProfile(
    UUID id,
    String subject,
    String username,
    String email
) {
    public static UserProfile create(String subject, String username, String email) {
        return new UserProfile(UUID.randomUUID(), subject, username, email);
    }

    public UserProfile update(String username, String email) {
        return new UserProfile(id, subject, username, email);
    }
}
