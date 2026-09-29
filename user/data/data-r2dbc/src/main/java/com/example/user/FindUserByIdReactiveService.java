package com.example.user;

import java.util.UUID;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FindUserByIdReactiveService implements FindUserByIdReactive {

    @Override
    public Mono<UserResponse> execute(UUID id) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }
}
