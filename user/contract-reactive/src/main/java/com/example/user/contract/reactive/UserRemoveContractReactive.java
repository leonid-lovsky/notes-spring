package com.example.user.contract.reactive;

import java.util.UUID;

import reactor.core.publisher.Mono;

public interface UserRemoveContractReactive {

    Mono<Void> remove(UUID id);

}
