package com.example.user.contract.reactive;

import java.util.UUID;

import reactor.core.publisher.Mono;

public interface UserExistsByIdContractReactive {

    Mono<Boolean> existsById(UUID id);

}
