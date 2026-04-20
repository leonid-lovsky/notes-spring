package com.example;

import com.example.application.crud.CrudService;
import com.example.application.note.NoteRequest;
import com.example.application.note.NoteResponse;
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

    private final CrudService<NoteRequest, NoteResponse, UUID> service;

    @PostMapping
    ResponseEntity<NoteResponse> create(@Valid @RequestBody NoteRequest noteRequest) {
        NoteResponse noteResponse = service.create(noteRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(noteResponse);
    }

    @GetMapping
    ResponseEntity<List<NoteResponse>> read() {
        List<NoteResponse> noteResponse = service.readAll();
        return ResponseEntity.ok(noteResponse);
    }

    @GetMapping("/{id}")
    ResponseEntity<NoteResponse> readById(@PathVariable UUID id) {
        NoteResponse noteResponse = service.readById(id);
        return ResponseEntity.ok(noteResponse);
    }

    @PatchMapping("/{id}")
    ResponseEntity<NoteResponse> updateById(@PathVariable UUID id, @Valid @RequestBody NoteRequest noteRequest) {
        NoteResponse noteResponse = service.updateById(id, noteRequest);
        return ResponseEntity.ok(noteResponse);
    }

    @PutMapping("/{id}")
    ResponseEntity<NoteResponse> replaceById(@PathVariable UUID id, @Valid @RequestBody NoteRequest noteRequest) {
        NoteResponse noteResponse = service.replaceById(id, noteRequest);
        return ResponseEntity.ok(noteResponse);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<NoteResponse> deleteById(@PathVariable UUID id) {
        NoteResponse noteResponse = service.deleteById(id);
        return ResponseEntity.ok(noteResponse);
    }
}
