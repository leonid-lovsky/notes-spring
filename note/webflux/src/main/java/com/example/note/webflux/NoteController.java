package com.example.note.webflux;

import java.util.UUID;

import com.example.note.contract.reactive.NoteContractReactive;
import com.example.note.domain.NoteNotFoundException;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notes")
class NoteController {

    private final NoteContractReactive noteContractReactive;

    NoteController(NoteContractReactive noteContractReactive) {
        this.noteContractReactive = noteContractReactive;
    }

    @PostMapping
    Mono<ResponseEntity<NoteResponse>> create(@RequestBody NoteRequest request) {
        return this.noteContractReactive.add(request)
            .map((note) -> ResponseEntity.status(HttpStatus.CREATED).body(note));
    }

    @GetMapping
    Flux<NoteResponse> findAll() {
        return this.noteContractReactive.findAll();
    }

    @GetMapping("/{id}")
    Mono<ResponseEntity<NoteResponse>> findById(@PathVariable UUID id) {
        return this.noteContractReactive.findById(id)
            .map((note) -> ResponseEntity.status(HttpStatus.OK).body(note))
            .switchIfEmpty(Mono.error(new NoteNotFoundException(id)));
    }

    @PutMapping("/{id}")
    Mono<ResponseEntity<NoteResponse>> update(@PathVariable UUID id, @RequestBody NoteRequest request) {
        return this.noteContractReactive.existsById(id)
            .flatMap((exists) -> exists
                    ? this.noteContractReactive.replace(id, request)
                        .map((updated) -> ResponseEntity.status(HttpStatus.OK).body(updated))
                    : Mono.error(new NoteNotFoundException(id)));
    }

    @DeleteMapping("/{id}")
    Mono<ResponseEntity<Void>> delete(@PathVariable UUID id) {
        return this.noteContractReactive.existsById(id)
            .flatMap((exists) -> exists
                    ? this.noteContractReactive.remove(id)
                        .thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).<Void>build())
                    : Mono.error(new NoteNotFoundException(id)));
    }

}
