package com.example.usernote.contract.reactive;

import java.util.UUID;

import reactor.core.publisher.Mono;

public interface UserNoteRemoveContractReactive {

    Mono<Void> remove(UUID userId, UUID noteId);

}
