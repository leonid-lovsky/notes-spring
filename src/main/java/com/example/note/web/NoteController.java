package com.example.note.web;

import com.example.note.CreateNoteCommand;
import com.example.note.NoteService;
import com.example.note.NoteView;
import com.example.note.UpdateNoteCommand;
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
    ResponseEntity<NoteView> create(@Valid @RequestBody CreateNoteRequest request) {
        NoteView note = service.createNote(new CreateNoteCommand(request.title(), request.content()));
        return ResponseEntity.status(HttpStatus.CREATED).body(note);
    }

    @GetMapping
    ResponseEntity<List<NoteView>> list() {
        return ResponseEntity.ok(service.getCurrentUserNotes());
    }

    @GetMapping("/{id}")
    ResponseEntity<NoteView> read(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getNote(id));
    }

    @PutMapping("/{id}")
    ResponseEntity<NoteView> update(@PathVariable UUID id, @Valid @RequestBody UpdateNoteRequest request) {
        NoteView note = service.updateNote(id, new UpdateNoteCommand(request.title(), request.content()));
        return ResponseEntity.ok(note);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deleteNote(id);
        return ResponseEntity.noContent().build();
    }
}
