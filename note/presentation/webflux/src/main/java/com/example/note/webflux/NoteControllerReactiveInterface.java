package com.example.note.webflux;

import com.example.note.contract.reactive.NoteReactiveInterface;
import com.example.note.domain.NoteResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.ResponseEntity;

public interface NoteControllerReactiveInterface extends NoteReactiveInterface<
    Mono<ResponseEntity<Boolean>>, Mono<ResponseEntity<NoteResponse>>, ResponseEntity<Flux<NoteResponse>>,
    Mono<ResponseEntity<Void>>> {

}
