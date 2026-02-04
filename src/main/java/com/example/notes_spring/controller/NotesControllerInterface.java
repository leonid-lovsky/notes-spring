package com.example.notes_spring.controller;

import com.example.notes_spring.controller.model.NotesRead;
import com.example.notes_spring.controller.model.NotesWrite;

import java.util.UUID;

public interface NotesControllerInterface {

    NotesRead create(NotesWrite record);

    NotesRead getById(UUID id);

    NotesRead update(UUID id, NotesWrite record);

    void delete(UUID id);
}
