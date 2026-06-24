package com.example.user.data.mongodb;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "users")
class UserDocument {

    @Id
    private UUID id;

    @Indexed(unique = true)
    private String username;

    @Indexed(unique = true)
    private String email;

    protected UserDocument() {

    }

    UserDocument(UUID id, String username, String email) {
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
