package com.example.user.contract.reactive;

import java.util.UUID;

import com.example.user.domain.UserResponse;
import reactor.core.publisher.Mono;

public interface UserFindByIdContractReactive {

    Mono<UserResponse> findById(UUID id);

}
