package com.example.note.presentation.rest;

import com.example.note.*;
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
@RequestMapping("${application.notes.path}")
@RequiredArgsConstructor
class NoteRestController {

    private final NoteCreateService noteCreateService;
    private final NoteReadService noteReadService;
    private final NoteUpdateService noteUpdateService;
    private final NoteReplaceService noteReplaceService;
    private final NoteDeleteService noteDeleteService;

    @PostMapping
    ResponseEntity<NoteResponseBody> create(@Valid @RequestBody NoteRequestBody noteRequestBody) {
        NoteResponseBody noteResponseBody = noteCreateService.create(noteRequestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(noteResponseBody);
    }

    @GetMapping
    ResponseEntity<List<NoteResponseBody>> read() {
        List<NoteResponseBody> noteResponseBody = noteReadService.read();
        return ResponseEntity.ok(noteResponseBody);
    }

    @GetMapping("/{id}")
    ResponseEntity<NoteResponseBody> read(@PathVariable UUID id) {
        NoteResponseBody noteResponseBody = noteReadService.read(id);
        return ResponseEntity.ok(noteResponseBody);
    }

    @PatchMapping("/{id}")
    ResponseEntity<NoteResponseBody> update(@PathVariable UUID id, @Valid @RequestBody NoteRequestBody noteRequestBody) {
        NoteResponseBody noteResponseBody = noteUpdateService.update(id, noteRequestBody);
        return ResponseEntity.ok(noteResponseBody);
    }

    @PutMapping("/{id}")
    ResponseEntity<NoteResponseBody> replace(@PathVariable UUID id, @Valid @RequestBody NoteRequestBody noteRequestBody) {
        NoteResponseBody noteResponseBody = noteReplaceService.replace(id, noteRequestBody);
        return ResponseEntity.ok(noteResponseBody);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<NoteResponseBody> delete(@PathVariable UUID id) {
        NoteResponseBody noteResponseBody = noteDeleteService.delete(id);
        return ResponseEntity.ok(noteResponseBody);
    }
}
