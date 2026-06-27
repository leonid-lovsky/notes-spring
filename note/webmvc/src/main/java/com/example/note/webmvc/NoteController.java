package com.example.note.webmvc;

import com.example.note.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notes")
class NoteController {

    private final NoteExistsById noteExistsById;
    private final NoteFindById noteFindById;
    private final NoteFindAll noteFindAll;
    private final NoteAdd noteAdd;
    private final NoteReplace noteReplace;
    private final NoteRemove noteRemove;

    NoteController(NoteExistsById noteExistsById, NoteFindById noteFindById, NoteFindAll noteFindAll,
                   NoteAdd noteAdd, NoteReplace noteReplace, NoteRemove noteRemove) {
        this.noteExistsById = noteExistsById;
        this.noteFindById = noteFindById;
        this.noteFindAll = noteFindAll;
        this.noteAdd = noteAdd;
        this.noteReplace = noteReplace;
        this.noteRemove = noteRemove;
    }

    @GetMapping
    ResponseEntity<List<NoteResponse>> findAll() {
        List<NoteResponse> notes = noteFindAll.findAll().stream()
            .map(NoteResponse::from)
            .toList();
        return ResponseEntity.status(HttpStatus.OK).body(notes);
    }

    @GetMapping("/{id}")
    ResponseEntity<NoteResponse> findById(@PathVariable UUID id) {
        Note note = noteFindById.findById(id)
            .orElseThrow(() -> new NoteNotFoundException(id));
        return ResponseEntity.status(HttpStatus.OK).body(NoteResponse.from(note));
    }

    @PostMapping
    ResponseEntity<NoteResponse> create(@RequestBody NoteRequest request) {
        Note note = noteAdd.add(new Note(null, request.content()));
        return ResponseEntity.status(HttpStatus.CREATED).body(NoteResponse.from(note));
    }

    @PutMapping("/{id}")
    ResponseEntity<NoteResponse> update(@PathVariable UUID id, @RequestBody NoteRequest request) {
        if (!noteExistsById.existsById(id)) {
            throw new NoteNotFoundException(id);
        }
        Note updated = new Note(id, request.content());
        noteReplace.replace(updated);
        return ResponseEntity.status(HttpStatus.OK).body(NoteResponse.from(updated));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!noteExistsById.existsById(id)) {
            throw new NoteNotFoundException(id);
        }
        noteRemove.remove(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
