package com.example.note;

import java.util.UUID;

public record NoteResponseBody(
    UUID id,
    String content
) {

}
