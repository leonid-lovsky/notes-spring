package com.example.usernote.feign;

import com.example.usernote.domain.UserClient;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class UserFeignClientFallback implements UserClient {

    @Override
    public Optional<UserSummary> findBySubject(String subject) {
        return Optional.empty();
    }
}
