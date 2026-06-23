package com.example.user.data.jpa;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "users")
class UserEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    protected UserEntity() {

    }

    UserEntity(UUID id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    UUID getId() {
        return id;
    }

    String getUsername() {
        return username;
    }

    String getEmail() {
        return email;
    }
}
