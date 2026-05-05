package com.example.note.contract;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteStore {

    Note create(Note note);

    List<Note> readAll();

    Optional<Note> readById(UUID id);

    Note update(Note note);

    Note replace(Note note);

    void deleteById(UUID id);
}
