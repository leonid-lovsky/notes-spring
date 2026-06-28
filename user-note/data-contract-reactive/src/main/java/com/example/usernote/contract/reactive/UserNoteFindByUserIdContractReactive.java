package com.example.usernote.contract.reactive;

import java.util.UUID;

import com.example.usernote.domain.UserNoteResponse;
import reactor.core.publisher.Flux;

public interface UserNoteFindByUserIdContractReactive {

    Flux<UserNoteResponse> findByUserId(UUID userId);

}
