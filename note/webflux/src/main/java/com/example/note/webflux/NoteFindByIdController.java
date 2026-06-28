package com.example.note.webflux;

import java.util.UUID;

import com.example.note.contract.reactive.NoteFindByIdContractReactive;
import com.example.note.domain.NoteNotFoundException;
import com.example.note.domain.NoteResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/notes")
class NoteFindByIdController {

    private final NoteFindByIdContractReactive noteFindByIdContractReactive;

    NoteFindByIdController(NoteFindByIdContractReactive noteFindByIdContractReactive) {
        this.noteFindByIdContractReactive = noteFindByIdContractReactive;
    }

    @GetMapping("/{id}")
    Mono<ResponseEntity<NoteResponse>> findById(@PathVariable UUID id) {
        return this.noteFindByIdContractReactive.findById(id)
            .map((note) -> ResponseEntity.status(HttpStatus.OK).body(note))
            .switchIfEmpty(Mono.error(new NoteNotFoundException(id)));
    }

}
