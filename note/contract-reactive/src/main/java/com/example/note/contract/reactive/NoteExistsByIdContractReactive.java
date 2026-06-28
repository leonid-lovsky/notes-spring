package com.example.note.contract.reactive;

import java.util.UUID;

import reactor.core.publisher.Mono;

public interface NoteExistsByIdContractReactive {

    Mono<Boolean> existsById(UUID id);

}
