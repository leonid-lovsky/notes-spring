package com.example.note.payload;

import java.util.UUID;

public record NoteResponse(
    UUID id,
    String content
) {

}
