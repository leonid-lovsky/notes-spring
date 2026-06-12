package com.example.user.domain;

import java.util.UUID;

public record User(UUID id, String username, String email, String password) {

    public User withUsername(String username) {
        return new User(this.id, username, this.email, this.password);
    }

    public User withEmail(String email) {
        return new User(this.id, this.username, email, this.password);
    }

    public User withPassword(String password) {
        return new User(this.id, this.username, this.email, password);
    }
}
