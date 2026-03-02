package com.example.application.notes;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${app.notes.base-url:/notes}")
@RequiredArgsConstructor
class NotesController {

    private final NotesService service;

    @GetMapping("/greeting")
    public String greeting() {
        return service.greet();
    }

    @GetMapping
    public List<NotesResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public NotesResponse findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NotesResponse create(@Valid @RequestBody NotesRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public NotesResponse updateById(@PathVariable UUID id, @Valid @RequestBody NotesRequest request) {
        return service.updateById(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable UUID id) {
        service.deleteById(id);
    }
}