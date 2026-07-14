package com.example.user.data.jdbc.model;

import java.util.UUID;

import com.example.user.domain.UserPersistable;
import org.jspecify.annotations.NullUnmarked;
import org.jspecify.annotations.Nullable;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@NullUnmarked
@Table("users")
public class UserJdbcEntity implements UserPersistable {

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
