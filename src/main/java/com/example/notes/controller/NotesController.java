package com.example.notes.controller;

import com.example.notes.payload.NotesInput;
import com.example.notes.payload.NotesOutput;
import com.example.notes.service.NotesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class NotesController {

    private final NotesService service;

    @GetMapping("/greeting")
    public String greeting() {
        return service.greet();
    }

    @GetMapping
    public List<NotesOutput> findAll() {
        return service.findAll();
    }

    @GetMapping(path = "/{id}")
    public NotesOutput findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NotesOutput create(@Valid @RequestBody NotesInput input) {
        return service.create(input);
    }

    @PutMapping(path = "/{id}")
    public NotesOutput replace(@PathVariable UUID id, @Valid @RequestBody NotesInput input) {
        return service.replace(id, input);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
