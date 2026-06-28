package com.example.note.webflux;

import com.example.note.contract.reactive.NoteFindAllContractReactive;
import com.example.note.domain.NoteResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/notes")
class NoteFindAllController {

    private final NoteFindAllContractReactive noteFindAllContractReactive;

    NoteFindAllController(NoteFindAllContractReactive noteFindAllContractReactive) {
        this.noteFindAllContractReactive = noteFindAllContractReactive;
    }

    @GetMapping
    Flux<NoteResponse> findAll() {
        return this.noteFindAllContractReactive.findAll();
    }

}
