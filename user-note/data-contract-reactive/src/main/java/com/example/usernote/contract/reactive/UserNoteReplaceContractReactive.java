package com.example.usernote.contract.reactive;

import java.util.UUID;

import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;
import reactor.core.publisher.Mono;

public interface UserNoteReplaceContractReactive {

    Mono<UserNoteResponse> replace(UUID userId, UUID noteId, UserNoteRequest request);

}
