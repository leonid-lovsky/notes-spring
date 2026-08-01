package com.example.user.data.mongodb.model;

import java.util.UUID;

import com.example.user.domain.UserPersistable;
import org.jspecify.annotations.NullUnmarked;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@NullUnmarked
@Document(collection = "users")
public class UserDocument implements UserPersistable {

    @Id
    private UUID id;

    @Indexed(unique = true)
    private String username;

    @Indexed(unique = true)
    private String email;

    protected UserDocument() {

    }

    public UserDocument(UUID id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    @Override
    public UUID getId() {
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
