package com.example.user.contract.reactive;

import com.example.user.domain.UserResponse;
import reactor.core.publisher.Flux;

public interface UserFindAllContractReactive {

    Flux<UserResponse> findAll();

}
