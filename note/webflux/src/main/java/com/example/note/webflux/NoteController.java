package com.example.note.webflux;

import java.util.UUID;

import com.example.note.contract.reactive.NoteServiceReactiveInterface;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

@RestController
@RequestMapping("/notes")
class NoteController implements NoteControllerReactiveInterface {

    private final NoteServiceReactiveInterface noteService;

    NoteController(NoteServiceReactiveInterface noteService) {
        this.noteService = noteService;
    }

    @Override
    @GetMapping("/{id}/exists")
    public Mono<ResponseEntity<Boolean>> existsById(@PathVariable UUID id) {
        return this.noteService.existsById(id).map((exists) -> ResponseEntity.status(HttpStatus.OK).body(exists));
    }

    @Override
    @PostMapping
    public Mono<ResponseEntity<NoteResponse>> add(@RequestBody NoteRequest request) {
        return this.noteService.add(request).map((note) -> ResponseEntity.status(HttpStatus.CREATED).body(note));
    }

    @Override
    @GetMapping
    public ResponseEntity<Flux<NoteResponse>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(this.noteService.findAll());
    }

    @Override
    @GetMapping("/{id}")
    public Mono<ResponseEntity<NoteResponse>> findById(@PathVariable UUID id) {
        return this.noteService.findById(id).map((note) -> ResponseEntity.status(HttpStatus.OK).body(note));
    }

    @Override
    @PutMapping("/{id}")
    public Mono<ResponseEntity<NoteResponse>> replace(@PathVariable UUID id, @RequestBody NoteRequest request) {
        return this.noteService.replace(id, request)
            .map((note) -> ResponseEntity.status(HttpStatus.OK).body(note));
    }

    @Override
    @PatchMapping("/{id}")
    public Mono<ResponseEntity<NoteResponse>> merge(@PathVariable UUID id, @RequestBody NoteRequest request) {
        return this.noteService.merge(id, request).map((note) -> ResponseEntity.status(HttpStatus.OK).body(note));
    }

    @Override
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> remove(@PathVariable UUID id) {
        return this.noteService.remove(id).thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).<Void>build());
    }
}
