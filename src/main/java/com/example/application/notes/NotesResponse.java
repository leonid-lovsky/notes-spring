package com.example.application.notes;

import java.util.UUID;

record NotesResponse(
    UUID id,
    String content
) {

}