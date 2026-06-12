package com.example.note.webmvc;

import com.example.note.domain.Note;
import com.example.note.domain.NoteOutputPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notes")
class NoteController {

    private final NoteOutputPort noteOutputPort;

    NoteController(NoteOutputPort noteOutputPort) {
        this.noteOutputPort = noteOutputPort;
    }

    @GetMapping
    ResponseEntity<List<NoteResponse>> findAll() {
        List<NoteResponse> notes = noteOutputPort.findAll().stream()
            .map(NoteResponse::from)
            .toList();
        return ResponseEntity.status(HttpStatus.OK).body(notes);
    }

    @GetMapping("/{id}")
    ResponseEntity<NoteResponse> findById(@PathVariable UUID id) {
        NoteResponse response = noteOutputPort.findById(id)
            .map(NoteResponse::from)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    ResponseEntity<NoteResponse> create(@RequestBody NoteRequest request) {
        Note note = new Note(UUID.randomUUID(), request.content());
        noteOutputPort.add(note);
        NoteResponse response = NoteResponse.from(note);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    ResponseEntity<NoteResponse> update(@PathVariable UUID id, @RequestBody NoteRequest request) {
        Note note = noteOutputPort.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Note updated = note.withContent(request.content());
        noteOutputPort.replace(updated);
        NoteResponse response = NoteResponse.from(updated);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!noteOutputPort.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        noteOutputPort.remove(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
