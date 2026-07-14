package com.example.user.data.r2dbc.model;

import java.util.UUID;

import org.jspecify.annotations.NullUnmarked;
import org.jspecify.annotations.Nullable;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@NullUnmarked
@Table("users")
public class UserR2dbcEntity {

    @Id
    private @Nullable UUID id;

    @Column("username")
    private String username;

    @Column("email")
    private String email;

    protected UserR2dbcEntity() {

    }

    public UserR2dbcEntity(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public UserR2dbcEntity(UUID id, String username, String email) {
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
