package com.example;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("/notes")
@RequiredArgsConstructor
class NoteController {

    private final NoteService service;

    @PostMapping
    ResponseEntity<NoteResponseBody> create(@Valid @RequestBody NoteRequestBody noteRequestBody) {
        NoteResponseBody noteResponseBody = service.create(noteRequestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(noteResponseBody);
    }

    @GetMapping
    ResponseEntity<List<NoteResponseBody>> read() {
        List<NoteResponseBody> noteResponseBodyList = service.readAll();
        return ResponseEntity.ok(noteResponseBodyList);
    }

    @GetMapping("/{id}")
    ResponseEntity<NoteResponseBody> readById(@PathVariable UUID id) {
        NoteResponseBody noteResponse = service.readByIdentity(id);
        return ResponseEntity.ok(noteResponse);
    }

    @PatchMapping("/{id}")
    ResponseEntity<NoteResponseBody> updateById(@PathVariable UUID id, @Valid @RequestBody NoteRequest noteRequest) {
        NoteResponseBody noteResponse = service.updateById(id, noteRequest);
        return ResponseEntity.ok(noteResponse);
    }

    @PutMapping("/{id}")
    ResponseEntity<NoteResponseBody> replaceById(@PathVariable UUID id, @Valid @RequestBody NoteRequest noteRequest) {
        NoteResponseBody noteResponse = service.replaceById(id, noteRequest);
        return ResponseEntity.ok(noteResponse);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<NoteResponseBody> deleteById(@PathVariable UUID id) {
        NoteResponseBody noteResponse = service.deleteById(id);
        return ResponseEntity.ok(noteResponse);
    }
}
