package com.example.usernote.data.mongodb.reactive.model;

import java.util.UUID;

import com.example.usernote.domain.UserNotePersistable;
import com.example.usernote.domain.UserNoteRole;
import org.jspecify.annotations.NullUnmarked;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@NullUnmarked
@Document(collection = "user_notes")
@CompoundIndex(name = "user_id_note_id_unique", def = "{'userId': 1, 'noteId': 1}", unique = true)
public class UserNoteReactiveDocument implements UserNotePersistable {

    @Id
    private UUID id;

    private UUID userId;

    private UUID noteId;

    private UserNoteRole role;

    protected UserNoteReactiveDocument() {

    }

    public UserNoteReactiveDocument(UUID id, UUID userId, UUID noteId, UserNoteRole role) {
        this.id = id;
        this.userId = userId;
        this.noteId = noteId;
        this.role = role;
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public UUID getUserId() {
        return this.userId;
    }

    @Override
    public UUID getNoteId() {
        return this.noteId;
    }

    @Override
    public UserNoteRole getRole() {
        return this.role;
    }
}
