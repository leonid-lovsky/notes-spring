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
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final FindNoteById findNoteById;
    private final CreateNoteById createNoteById;
    private final ReplaceNoteById replaceNoteById;
    private final UpdateNoteById updateNoteById;
    private final DeleteNoteById deleteNoteById;

    public NoteController(FindNoteById findNoteById, CreateNoteById createNoteById, ReplaceNoteById replaceNoteById, UpdateNoteById updateNoteById, DeleteNoteById deleteNoteById) {
        this.findNoteById = findNoteById;
        this.createNoteById = createNoteById;
        this.replaceNoteById = replaceNoteById;
        this.updateNoteById = updateNoteById;
        this.deleteNoteById = deleteNoteById;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<NoteResponse>> findNoteById(@PathVariable("id") UUID id) {
        return findNoteById.findNoteById(id).map(ResponseEntity::ok);
    }

    @PostMapping("/{id}")
    public Mono<ResponseEntity<NoteResponse>> createNoteById(@PathVariable("id") UUID id, @RequestBody NoteRequest request) {
        return createNoteById.createNoteById(id, request).map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<NoteResponse>> replaceNoteById(@PathVariable("id") UUID id, @RequestBody NoteRequest request) {
        return replaceNoteById.replaceNoteById(id, request).map(ResponseEntity::ok);
    }

    @PatchMapping(path = "/{id}")
    public Mono<ResponseEntity<NoteResponse>> updateNoteById(@PathVariable("id") UUID id, @RequestBody NoteRequest request) {
        return updateNoteById.updateNoteById(id, request).map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<NoteResponse>> deleteNoteById(@PathVariable("id") UUID id) {
        return deleteNoteById.deleteNoteById(id).map(ResponseEntity::ok);
    }
}
