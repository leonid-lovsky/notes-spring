package com.example.user.contract.reactive;

import com.example.user.domain.UserResponse;

import reactor.core.publisher.Mono;

public interface UserFindByUsernameContractReactive {

    Mono<UserResponse> findByUsername(String username);

}
