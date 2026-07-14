package com.example.usernote.data.mongodb.model;

import java.util.UUID;

import com.example.usernote.domain.UserNoteRole;
import org.jspecify.annotations.NullUnmarked;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@NullUnmarked
@Document(collection = "user_notes")
@CompoundIndex(name = "user_id_note_id_unique", def = "{'userId': 1, 'noteId': 1}", unique = true)
public class UserNoteDocument {

    @Id
    private UUID id;

    private UUID userId;

    private UUID noteId;

    private UserNoteRole role;

    protected UserNoteDocument() {

    }

    public UserNoteDocument(UUID id, UUID userId, UUID noteId, UserNoteRole role) {
        this.id = id;
        this.userId = userId;
        this.noteId = noteId;
        this.role = role;
    }

    public UUID getId() {
        return this.id;
    }

    public UUID getUserId() {
        return this.userId;
    }

    public UUID getNoteId() {
        return this.noteId;
    }

    public UserNoteRole getRole() {
        return this.role;
    }
}
