package com.example.user;

import java.util.UUID;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ReplaceUserByIdReactiveService implements ReplaceUserByIdReactive {

    @Override
    public Mono<UserResponse> execute(UUID id, UserRequest request) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }
}
