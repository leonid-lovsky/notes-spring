package com.example.user.contract.reactive;

import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;
import reactor.core.publisher.Mono;

public interface UserAddContractReactive {

    Mono<UserResponse> add(UserRequest request);

}
