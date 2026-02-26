package com.example.notes.controller;

import com.example.notes.payload.NotesPayload;
import com.example.notes.payload.NotesResponse;
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
    public List<NotesResponse> findAll() {
        return service.findAll();
    }

    @GetMapping(path = "/{id}")
    public NotesResponse findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NotesResponse create(@Valid @RequestBody NotesPayload payload) {
        return service.create(payload);
    }

    @PutMapping(path = "/{id}")
    public NotesResponse updateById(@PathVariable UUID id, @Valid @RequestBody NotesPayload payload) {
        return service.updateById(id, payload);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable UUID id) {
        service.deleteById(id);
    }
}
