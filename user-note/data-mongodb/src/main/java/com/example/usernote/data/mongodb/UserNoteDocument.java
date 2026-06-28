package com.example.usernote.data.mongodb;

import com.example.usernote.domain.UserNoteRole;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user_notes")
class UserNoteDocument {

    @Id
    private UserNoteKey id;

    private UserNoteRole role;

    @SuppressWarnings("NullAway.Init")
    protected UserNoteDocument() {

    }

    UserNoteDocument(UserNoteKey id, UserNoteRole role) {
        this.id = id;
        this.role = role;
    }

    UserNoteKey getId() {
        return this.id;
    }

    UserNoteRole getRole() {
        return this.role;
    }

}
