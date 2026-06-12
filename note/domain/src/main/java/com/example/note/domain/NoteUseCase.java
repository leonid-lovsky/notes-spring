package com.example.note.domain;

import java.util.List;
import java.util.UUID;

public interface NoteUseCase {

    Note create(String content);

    Note findById(UUID id);

    List<Note> findAll();

    Note update(UUID id, String content);

    void delete(UUID id);
}
