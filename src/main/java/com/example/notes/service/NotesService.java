package com.example.notes.service;

import com.example.notes.payload.NotesRead;
import com.example.notes.payload.NotesWrite;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
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

    public void delete(UUID id) {
    }
}
