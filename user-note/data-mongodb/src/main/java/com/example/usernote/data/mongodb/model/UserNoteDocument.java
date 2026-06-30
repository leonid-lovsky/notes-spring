package com.example.usernote.data.mongodb.model;

import com.example.usernote.domain.UserNoteRole;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user_notes")
public class UserNoteDocument {

    @Id
    private UserNoteKey id;

    private UserNoteRole role;

    @SuppressWarnings("NullAway.Init")
    protected UserNoteDocument() {

    }

    public UserNoteDocument(UserNoteKey id, UserNoteRole role) {
        this.id = id;
        this.role = role;
    }

    public UserNoteKey getId() {
        return this.id;
    }

    public UserNoteRole getRole() {
        return this.role;
    }

}
