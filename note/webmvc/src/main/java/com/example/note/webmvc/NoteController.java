package com.example.note.webmvc;

import com.example.note.domain.Note;
import com.example.note.domain.NoteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
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
    List<NoteResponse> findAll() {
        return noteRepository.findAll().stream()
            .map(NoteResponse::from)
            .toList();
    }

    @GetMapping("/{id}")
    NoteResponse findById(@PathVariable UUID id) {
        return noteRepository.findById(id)
            .map(NoteResponse::from)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    ResponseEntity<NoteResponse> create(@RequestBody NoteRequest request) {
        Note note = noteRepository.save(new Note(UUID.randomUUID(), request.content()));
        NoteResponse response = NoteResponse.from(note);
        return ResponseEntity.created(URI.create("/notes/" + response.id())).body(response);
    }

    @PutMapping("/{id}")
    NoteResponse update(@PathVariable UUID id, @RequestBody NoteRequest request) {
        Note note = noteRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        note.setContent(request.content());
        return NoteResponse.from(noteRepository.save(note));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable UUID id) {
        noteRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        noteRepository.deleteById(id);
    }
}
