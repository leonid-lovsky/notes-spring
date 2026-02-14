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

    @RequestMapping(method = RequestMethod.POST)
    public NotesRead create(@RequestBody NotesWrite payload) {
        return service.create(payload);
    }

    @RequestMapping(method = RequestMethod.GET, path = "{id}")
    public NotesRead getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "{id}")
    public NotesRead update(@PathVariable UUID id, @RequestBody NotesWrite payload) {
        return service.update(id, payload);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
