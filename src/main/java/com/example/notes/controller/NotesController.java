package com.example.notes.controller;

import com.example.notes.payload.NotesRead;
import com.example.notes.payload.NotesWrite;
import com.example.notes.service.NotesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class NotesController {

    private final NotesService service;

    @PostMapping
    public NotesRead create(@RequestBody NotesWrite payload) {
        return service.create(payload);
    }

    @GetMapping(path = "{id}")
    public NotesRead getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PutMapping(path = "{id}")
    public NotesRead update(@PathVariable UUID id, @RequestBody NotesWrite payload) {
        return service.update(id, payload);
    }

    @DeleteMapping(path = "{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
