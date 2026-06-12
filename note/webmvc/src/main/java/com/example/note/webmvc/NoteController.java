package com.example.note.webmvc;

import com.example.note.domain.Note;
import com.example.note.domain.NoteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notes")
class NoteController {

    private final NoteRepository noteRepository;

    NoteController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @GetMapping
    ResponseEntity<List<NoteResponse>> findAll() {
        List<NoteResponse> notes = noteRepository.findAll().stream()
            .map(NoteResponse::from)
            .toList();
        return ResponseEntity.status(HttpStatus.OK).body(notes);
    }

    @GetMapping("/{id}")
    ResponseEntity<NoteResponse> findById(@PathVariable UUID id) {
        NoteResponse response = noteRepository.findById(id)
            .map(NoteResponse::from)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    ResponseEntity<NoteResponse> create(@RequestBody NoteRequest request) {
        Note note = new Note(UUID.randomUUID(), request.content());
        noteRepository.add(note);
        return ResponseEntity.status(HttpStatus.CREATED).body(NoteResponse.from(note));
    }

    @PutMapping("/{id}")
    ResponseEntity<NoteResponse> update(@PathVariable UUID id, @RequestBody NoteRequest request) {
        Note note = noteRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Note updated = note.withContent(request.content());
        noteRepository.replace(updated);
        return ResponseEntity.status(HttpStatus.OK).body(NoteResponse.from(updated));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!noteRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        noteRepository.remove(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
