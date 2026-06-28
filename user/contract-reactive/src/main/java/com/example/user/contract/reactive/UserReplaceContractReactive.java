package com.example.user.contract.reactive;

import java.util.UUID;

import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

import reactor.core.publisher.Mono;

public interface UserReplaceContractReactive {

    Mono<UserResponse> replace(UUID id, UserRequest request);

}
