package com.example.note.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteRepository {

    boolean existsById(UUID id);

    Optional<Note> findById(UUID id);

    List<Note> findAll();

    void add(Note note);

    void replace(Note note);

    void remove(UUID id);
}
