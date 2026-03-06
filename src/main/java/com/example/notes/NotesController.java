package com.example.notes;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("${app.notes.base-url}")
@RequiredArgsConstructor
@Slf4j
class NotesController {

    Logger logger = LoggerFactory.getLogger(NotesController.class);

    private final NotesService service;

    @GetMapping("/greeting")
    public ResponseEntity<String> greeting() {
        log.info("Received greeting request");
        return ResponseEntity.ok(service.greet());
    }

    @GetMapping
    public ResponseEntity<List<NotesResponse>> findAll() {
        log.info("Received request to get all notes");
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotesResponse> findById(@PathVariable UUID id) {
        log.info("Received request to get note with id={}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<NotesResponse> create(@Valid @RequestBody NotesRequest request) {
        log.info("Creating new note: {}", request);
        NotesResponse created = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotesResponse> updateById(
        @PathVariable UUID id,
        @Valid @RequestBody NotesRequest request
    ) {
        log.info("Updating note with id={}: {}", id, request);
        return ResponseEntity.ok(service.updateById(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        log.info("Deleting note with id={}", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

