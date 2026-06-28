package com.example.note.webflux;

import com.example.note.contract.reactive.NoteAddContractReactive;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/notes")
class NoteCreateController {

    private final NoteAddContractReactive noteAddContractReactive;

    NoteCreateController(NoteAddContractReactive noteAddContractReactive) {
        this.noteAddContractReactive = noteAddContractReactive;
    }

    @PostMapping
    Mono<ResponseEntity<NoteResponse>> create(@RequestBody NoteRequest request) {
        return this.noteAddContractReactive.add(request)
            .map((note) -> ResponseEntity.status(HttpStatus.CREATED).body(note));
    }

}
