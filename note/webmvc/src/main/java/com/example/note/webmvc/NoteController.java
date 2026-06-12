package com.example.note.webmvc;

import com.example.note.domain.Note;
import com.example.note.domain.NoteUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/notes")
class NoteController {

    private final NoteUseCase noteUseCase;

    NoteController(NoteUseCase noteUseCase) {
        this.noteUseCase = noteUseCase;
    }

    @GetMapping
    ResponseEntity<List<NoteResponse>> findAll() {
        List<NoteResponse> notes = noteUseCase.findAll().stream()
            .map(NoteResponse::from)
            .toList();
        return ResponseEntity.status(HttpStatus.OK).body(notes);
    }

    @GetMapping("/{id}")
    ResponseEntity<NoteResponse> findById(@PathVariable UUID id) {
        NoteResponse response = NoteResponse.from(noteUseCase.findById(id));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    ResponseEntity<NoteResponse> create(@RequestBody NoteRequest request) {
        Note note = noteUseCase.create(request.content());
        NoteResponse response = NoteResponse.from(note);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    ResponseEntity<NoteResponse> update(@PathVariable UUID id, @RequestBody NoteRequest request) {
        Note updated = noteUseCase.update(id, request.content());
        NoteResponse response = NoteResponse.from(updated);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id) {
        noteUseCase.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    ResponseEntity<Void> handleNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
