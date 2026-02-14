package com.example.notes.payload;

import java.util.Date;
import java.util.UUID;

public record NotesRead(
    UUID id,
    String content,
    Date createdAt,
    Date updatedAt
) {

}
