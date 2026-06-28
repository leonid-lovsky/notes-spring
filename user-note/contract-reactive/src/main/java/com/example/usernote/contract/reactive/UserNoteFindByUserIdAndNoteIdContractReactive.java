package com.example.usernote.contract.reactive;

import java.util.UUID;

import com.example.usernote.domain.UserNoteResponse;

import reactor.core.publisher.Mono;

public interface UserNoteFindByUserIdAndNoteIdContractReactive {

    Mono<UserNoteResponse> findByUserIdAndNoteId(UUID userId, UUID noteId);

}
