package com.example.note.domain;

import java.util.UUID;

public final class Note {

    private final UUID id;
    private String content;

    public Note(UUID id, String content) {
        this.id = id;
        this.content = content;
    }

    public UUID getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
