package com.example.user.data.jdbc.model;

import java.util.UUID;

import org.jspecify.annotations.NullUnmarked;
import org.jspecify.annotations.Nullable;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@NullUnmarked
@Table("users")
public class UserJdbcEntity {

    @Id
    private @Nullable UUID id;

    @Column("username")
    private String username;

    @Column("email")
    private String email;

    protected UserJdbcEntity() {

    }

    public UserJdbcEntity(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public UserJdbcEntity(UUID id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public @Nullable UUID getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }
}
