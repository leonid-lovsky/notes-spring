package com.example.note.controller;

import com.example.note.NoteRequestBody;
import com.example.note.NoteResponseBody;
import com.example.note.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController @Validated
@RequestMapping("${application.notes.path}")
@RequiredArgsConstructor
class NoteRestController {

    private final NoteService service;

    @PostMapping
    ResponseEntity<NoteResponseBody> create(@Valid @RequestBody NoteRequestBody requestBody) {
        NoteResponseBody responseBody = service.create(requestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @GetMapping
    ResponseEntity<List<NoteResponseBody>> read() {
        List<NoteResponseBody> responseBody = service.read();
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/{id}")
    ResponseEntity<NoteResponseBody> read(@PathVariable UUID id) {
        NoteResponseBody responseBody = service.read(id);
        return ResponseEntity.ok(responseBody);
    }

    @PatchMapping("/{id}")
    ResponseEntity<NoteResponseBody> update(@PathVariable UUID id, @Valid @RequestBody NoteRequestBody requestBody) {
        NoteResponseBody responseBody = service.update(id, requestBody);
        return ResponseEntity.ok(responseBody);
    }

    @PutMapping("/{id}")
    ResponseEntity<NoteResponseBody> replace(@PathVariable UUID id, @Valid @RequestBody NoteRequestBody requestBody) {
        NoteResponseBody responseBody = service.replace(id, requestBody);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<NoteResponseBody> delete(@PathVariable UUID id) {
        NoteResponseBody responseBody = service.delete(id);
        return ResponseEntity.ok(responseBody);
    }
}
