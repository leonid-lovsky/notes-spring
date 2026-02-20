package com.example.notes.service;

import com.example.notes.payload.NotesInput;
import com.example.notes.payload.NotesOutput;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class NotesService {

    public String greet() {
        return "Hello, World";
    }

    public List<NotesOutput> findAll() {
        return null;
    }

    public NotesOutput findById(UUID id) {
        return null;
    }

    public NotesOutput create(NotesInput input) {
        return null;
    }

    public NotesOutput replace(UUID id, NotesInput input) {
        return null;
    }

    public void delete(UUID id) {

    }
}
