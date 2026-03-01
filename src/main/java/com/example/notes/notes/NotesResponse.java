package com.example.notes.notes;

import java.util.UUID;

public record NotesResponse(
    UUID id,
    String content
) {

}
