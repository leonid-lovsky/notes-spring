package com.example.usernote.contract.reactive;

import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;
import reactor.core.publisher.Mono;

public interface UserNoteAddContractReactive {

    Mono<UserNoteResponse> add(UserNoteRequest request);

}
