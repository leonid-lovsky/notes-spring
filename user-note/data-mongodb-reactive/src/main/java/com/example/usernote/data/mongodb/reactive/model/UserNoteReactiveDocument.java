package com.example.usernote.data.mongodb.reactive.model;

import com.example.usernote.domain.UserNoteRole;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user_notes")
public class UserNoteReactiveDocument {

    @Id
    private UserNoteReactiveKey id;

    private UserNoteRole role;

    @SuppressWarnings("NullAway.Init")
    protected UserNoteReactiveDocument() {

    }

    public UserNoteReactiveDocument(UserNoteReactiveKey id, UserNoteRole role) {
        this.id = id;
        this.role = role;
    }

    public UserNoteReactiveKey getId() {
        return this.id;
    }

    public UserNoteRole getRole() {
        return this.role;
    }

}
