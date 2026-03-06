package com.example.notes;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("${app.notes.base-url}")
@RequiredArgsConstructor
class NotesController {

    private final NotesService service;

    @GetMapping("/greeting")
    public ResponseEntity<String> greeting() {
        var result = service.greet();
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<NotesResponse> create(@Valid @RequestBody NotesRequest request) {
        var result = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotesResponse> read(@PathVariable UUID id) {
        var result = service.read(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotesResponse> update(@PathVariable UUID id, @Valid @RequestBody NotesRequest request) {
        var result = service.update(id, request);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
