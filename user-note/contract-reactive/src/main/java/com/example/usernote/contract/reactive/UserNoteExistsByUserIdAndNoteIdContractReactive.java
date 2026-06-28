package com.example.usernote.contract.reactive;

import java.util.UUID;

import reactor.core.publisher.Mono;

public interface UserNoteExistsByUserIdAndNoteIdContractReactive {

    Mono<Boolean> existsByUserIdAndNoteId(UUID userId, UUID noteId);

}
