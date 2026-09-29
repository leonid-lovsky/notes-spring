package com.example.note;

import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final FindNoteByIdSynchronous findNoteById;
    private final CreateNoteByIdSynchronous createNoteById;
    private final ReplaceNoteByIdSynchronous replaceNoteById;
    private final UpdateNoteByIdSynchronous updateNoteById;
    private final DeleteNoteByIdSynchronous deleteNoteById;

    public NoteController(FindNoteByIdSynchronous findNoteById, CreateNoteByIdSynchronous createNoteById, ReplaceNoteByIdSynchronous replaceNoteById, UpdateNoteByIdSynchronous updateNoteById, DeleteNoteByIdSynchronous deleteNoteById) {
        this.findNoteById = findNoteById;
        this.createNoteById = createNoteById;
        this.replaceNoteById = replaceNoteById;
        this.updateNoteById = updateNoteById;
        this.deleteNoteById = deleteNoteById;
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponse> findNoteById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(findNoteById.execute(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<NoteResponse> createNoteById(@PathVariable("id") UUID id, @RequestBody NoteRequest request) {
        return ResponseEntity.ok(createNoteById.execute(id, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteResponse> replaceNoteById(@PathVariable("id") UUID id, @RequestBody NoteRequest request) {
        return ResponseEntity.ok(replaceNoteById.execute(id, request));
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<NoteResponse> updateNoteById(@PathVariable("id") UUID id, @RequestBody NoteRequest request) {
        return ResponseEntity.ok(updateNoteById.execute(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<NoteResponse> deleteNoteById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(deleteNoteById.execute(id));
    }
}
