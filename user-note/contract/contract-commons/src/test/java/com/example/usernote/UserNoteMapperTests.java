package com.example.usernote;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserNoteMapperTests {

    private final UserNoteMapper userNoteMapper = new UserNoteMapperImpl();

    @Test
    void toResponse() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNote userNote = new UserNote(id, userId, noteId);
        UserNoteResponse response = userNoteMapper.toResponse(userNote);

        UserNoteResponse expected = new UserNoteResponse(id, userId, noteId);

        assertEquals(expected, response);
    }

    @Test
    void toEntity() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNoteRequest request = new UserNoteRequest(userId, noteId);
        UserNote userNote = userNoteMapper.toEntity(id, request);

        UserNote expected = new UserNote(id, userId, noteId);

        assertEquals(expected, userNote);
    }
}
