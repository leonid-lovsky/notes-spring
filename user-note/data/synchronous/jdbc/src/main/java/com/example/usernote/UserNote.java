package com.example.usernote;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("user_note")
public record UserNote(
    @Id @Column("id") UUID id,
    @Column("user_id") UUID userId,
    @Column("note_id") UUID noteId,
    @Column("role") UserNoteRole role
) {

    public UserNoteResponseBody responseBody() {
        return new UserNoteResponseBody(id, userId, noteId, role);
    }
}
