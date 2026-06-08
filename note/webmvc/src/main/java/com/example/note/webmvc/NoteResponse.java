package com.example.note.webmvc;

import com.example.note.domain.Note;
import java.util.UUID;

record NoteResponse(UUID id, String content) {

    static NoteResponse from(Note note) {
        return new NoteResponse(note.getId(), note.getContent());
    }
}
