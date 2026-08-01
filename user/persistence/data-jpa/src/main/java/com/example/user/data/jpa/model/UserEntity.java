package com.example.user.data.jpa.model;

import java.util.UUID;

import com.example.user.domain.UserPersistable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.jspecify.annotations.NullUnmarked;
import org.jspecify.annotations.Nullable;

@NullUnmarked
@Entity
@Table(name = "users")
public class UserEntity implements UserPersistable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private @Nullable UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    protected UserEntity() {

    }

    public UserEntity(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public UserEntity(UUID id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    @Override
    public @Nullable UUID getId() {
        return this.id;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getEmail() {
        return this.email;
    }
}
