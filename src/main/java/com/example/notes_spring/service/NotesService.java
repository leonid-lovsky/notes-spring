package com.example.notes_spring.service;

import com.example.notes_spring.payload.NotesRead;
import com.example.notes_spring.payload.NotesWrite;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NotesService {

    public NotesRead create(NotesWrite payload) {
        return null;
    }

    public NotesRead getById(UUID id) {
        return null;
    }

    public NotesRead update(UUID id, NotesWrite payload) {
        return null;
    }

    public NotesRead delete(UUID id) {
        return null;
    }
}
