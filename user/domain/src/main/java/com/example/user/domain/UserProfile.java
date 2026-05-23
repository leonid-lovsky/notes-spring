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

    public UserProfile withUsername(String username) {
        return new UserProfile(id, subject, username, email);
    }

    public UserProfile withEmail(String email) {
        return new UserProfile(id, subject, username, email);
    }
}
