package com.example.usernote.feign;

import com.example.usernote.domain.UserClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "user-service", fallback = UserFeignClientFallback.class)
public interface UserFeignClient extends UserClient {

    @GetMapping("/users/{subject}")
    @Override
    Optional<UserSummary> findBySubject(@PathVariable String subject);
}
