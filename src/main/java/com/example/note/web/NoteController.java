package com.example.note.web;

import com.example.note.NoteResponse;
import com.example.note.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
class NoteController {

    private final NoteService service;

    @PostMapping
    ResponseEntity<NoteResponse> create(@Valid @RequestBody CreateNoteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping
    ResponseEntity<List<NoteResponse>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{id}")
    ResponseEntity<NoteResponse> read(@PathVariable UUID id) {
        return ResponseEntity.ok(service.read(id));
    }

    @PatchMapping("/{id}")
    ResponseEntity<NoteResponse> update(@PathVariable UUID id, @RequestBody UpdateNoteRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @PutMapping("/{id}")
    ResponseEntity<NoteResponse> replace(@PathVariable UUID id, @Valid @RequestBody ReplaceNoteRequest request) {
        return ResponseEntity.ok(service.replace(id, request));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
