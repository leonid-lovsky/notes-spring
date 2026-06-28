package com.example.user.data.mongodb;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
class UserDocument {

    @Id
    private UUID id;

    @Indexed(unique = true)
    private String username;

    @Indexed(unique = true)
    private String email;

    @SuppressWarnings("NullAway.Init")
    protected UserDocument() {

    }

    UserDocument(UUID id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    UUID getId() {
        return this.id;
    }

    String getUsername() {
        return this.username;
    }

    String getEmail() {
        return this.email;
    }

}
