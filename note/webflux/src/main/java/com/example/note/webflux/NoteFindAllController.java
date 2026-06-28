package com.example.note.webflux;

import com.example.note.contract.reactive.NoteFindAllContractReactive;
import com.example.note.domain.NoteResponse;
import reactor.core.publisher.Flux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
