package com.example.note;

import java.util.UUID;

public record NoteResponse(
    UUID id,
    String content
) {

}
