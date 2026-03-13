package com.example.note;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("${app.notes.base-url}")
class NoteController {

    private final NoteService service;

    NoteController(NoteService service) {
        this.service = service;
    }

    @GetMapping("/greeting")
    ResponseEntity<String> greeting() {
        var result = service.greet();
        return ResponseEntity.ok(result);
    }

    @PostMapping
    ResponseEntity<NoteResponse> create(@Valid @RequestBody NoteRequest request) {
        var result = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{id}")
    ResponseEntity<NoteResponse> read(@PathVariable UUID id) {
        var result = service.read(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    ResponseEntity<NoteResponse> update(@PathVariable UUID id, @Valid @RequestBody NoteRequest request) {
        var result = service.update(id, request);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
