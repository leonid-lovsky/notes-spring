package com.example.note;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final FindNoteById findNoteById;
    private final CreateNoteById createNoteById;
    private final ReplaceNoteById replaceNoteById;
    private final UpdateNoteById updateNoteById;
    private final DeleteNoteById deleteNoteById;

    public NoteController(
        FindNoteById findNoteById,
        CreateNoteById createNoteById,
        ReplaceNoteById replaceNoteById,
        UpdateNoteById updateNoteById,
        DeleteNoteById deleteNoteById
    ) {
        this.findNoteById = findNoteById;
        this.createNoteById = createNoteById;
        this.replaceNoteById = replaceNoteById;
        this.updateNoteById = updateNoteById;
        this.deleteNoteById = deleteNoteById;
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponse> findNoteById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(findNoteById.findNoteById(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<NoteResponse> createNoteById(@PathVariable("id") UUID id, @RequestBody NoteRequest request) {
        return ResponseEntity.ok(createNoteById.createNoteById(id, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteResponse> replaceNoteById(@PathVariable("id") UUID id, @RequestBody NoteRequest request) {
        return ResponseEntity.ok(replaceNoteById.replaceNoteById(id, request));
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<NoteResponse> updateNoteById(@PathVariable("id") UUID id, @RequestBody NoteRequest request) {
        return ResponseEntity.ok(updateNoteById.updateNoteById(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<NoteResponse> deleteNoteById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(deleteNoteById.deleteNoteById(id));
    }
}
