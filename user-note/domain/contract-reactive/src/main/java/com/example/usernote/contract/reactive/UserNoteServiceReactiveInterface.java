package com.example.usernote.contract.reactive;

import com.example.usernote.domain.UserNoteResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserNoteServiceReactiveInterface extends UserNoteReactiveInterface<
    Mono<Boolean>, Mono<UserNoteResponse>, Flux<UserNoteResponse>, Mono<Void>> {

}
