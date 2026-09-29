package com.example.note;

import java.util.UUID;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/notes")
public class NoteController {

    @GetMapping("/{id}")
    public Mono<ResponseEntity<NoteResponse>> findNoteById(@PathVariable("id") UUID id) {
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED));
    }

    @PostMapping
    public Mono<ResponseEntity<NoteResponse>> createNote(@RequestBody NoteRequest request) {
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<NoteResponse>> replaceNoteById(@PathVariable("id") UUID id, @RequestBody NoteRequest request) {
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED));
    }

    @PatchMapping(path = "/{id}")
    public Mono<ResponseEntity<NoteResponse>> updateNoteById(@PathVariable("id") UUID id, @RequestBody NoteRequest request) {
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteNoteById(@PathVariable("id") UUID id) {
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED));
    }
}
