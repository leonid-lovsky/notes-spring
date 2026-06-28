package com.example.user.data.jpa;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.jspecify.annotations.Nullable;

@Entity
@Table(name = "users")
class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private @Nullable UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @SuppressWarnings("NullAway.Init")
    protected UserEntity() {

    }

    UserEntity(String username, String email) {
        this.username = username;
        this.email = email;
    }

    UserEntity(UUID id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    @Nullable UUID getId() {
        return this.id;
    }

    String getUsername() {
        return this.username;
    }

    String getEmail() {
        return this.email;
    }

}
