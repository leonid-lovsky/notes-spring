package com.example.user.domain;

import java.util.UUID;

public record User(UUID id, String username, String email) {

    public User withUsername(String username) {
        return new User(this.id, username, this.email);
    }

    public User withEmail(String email) {
        return new User(this.id, this.username, email);
    }
}
