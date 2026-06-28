package com.example.note.contract.reactive;

import java.util.UUID;

import reactor.core.publisher.Mono;

public interface NoteRemoveContractReactive {

    Mono<Void> remove(UUID id);

}
