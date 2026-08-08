package com.example.note.contract.reactive;

import com.example.note.domain.NoteResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NoteServiceReactiveInterface extends
    NoteReactiveInterface<Mono<Boolean>, Mono<NoteResponse>, Flux<NoteResponse>, Mono<Void>> {

}
