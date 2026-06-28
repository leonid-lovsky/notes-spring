package com.example.user.contract.reactive;

import com.example.user.domain.UserResponse;
import reactor.core.publisher.Mono;

public interface UserFindByEmailContractReactive {

    Mono<UserResponse> findByEmail(String email);

}
