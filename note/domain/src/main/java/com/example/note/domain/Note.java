package com.example.note.domain;

import java.util.UUID;

public record Note(UUID id, String content) {

    public Note withContent(String content) {
        return new Note(this.id, content);
    }
}
