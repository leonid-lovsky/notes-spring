package com.example.usernote;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Document("user_note")
public record UserNote(
    @Id UUID id,
    @Field("user_id") UUID userId,
    @Field("note_id") UUID noteId,
    @Field("role") UserNoteRole role
) {

    public UserNoteResponseBody responseBody() {
        return new UserNoteResponseBody(id, userId, noteId, role);
    }
}
