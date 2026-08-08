package com.example.usernote.webflux;

import com.example.usernote.contract.reactive.UserNoteReactiveInterface;
import com.example.usernote.domain.UserNoteResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.ResponseEntity;

public interface UserNoteControllerReactiveInterface extends UserNoteReactiveInterface<
    Mono<ResponseEntity<Boolean>>, Mono<ResponseEntity<UserNoteResponse>>, ResponseEntity<Flux<UserNoteResponse>>,
    Mono<ResponseEntity<Void>>> {

}
