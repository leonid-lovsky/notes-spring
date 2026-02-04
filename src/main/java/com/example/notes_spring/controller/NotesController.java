package com.example.notes_spring.controller;

import com.example.notes_spring.controller.model.NotesRead;
import com.example.notes_spring.controller.model.NotesWrite;
import com.example.notes_spring.service.NotesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class NotesController implements NotesControllerInterface {

    private final NotesService service;

    @Override
    public NotesRead create(NotesWrite record) {
        return null;
    }

    @Override
    public NotesRead getById(UUID id) {
        return null;
    }

    @Override
    public NotesRead update(UUID id, NotesWrite record) {
        return null;
    }

    @Override
    public void delete(UUID id) {

    }
}
