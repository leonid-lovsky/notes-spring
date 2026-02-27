package com.example.notes.payload;

import java.util.UUID;

public record NotesResponse(
    UUID id,
    String content
) {

}
