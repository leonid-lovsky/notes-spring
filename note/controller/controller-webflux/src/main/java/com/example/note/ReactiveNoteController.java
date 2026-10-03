package com.example.note;

import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/notes")
public class ReactiveNoteController {

    private final ReactiveFindNoteById findNoteById;
    private final ReactiveCreateNoteById createNoteById;
    private final ReactiveReplaceNoteById replaceNoteById;
    private final ReactiveUpdateNoteById updateNoteById;
    private final ReactiveDeleteNoteById deleteNoteById;

    public ReactiveNoteController(
        ReactiveFindNoteById findNoteById,
        ReactiveCreateNoteById createNoteById,
        ReactiveReplaceNoteById replaceNoteById,
        ReactiveUpdateNoteById updateNoteById,
        ReactiveDeleteNoteById deleteNoteById
    ) {
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
