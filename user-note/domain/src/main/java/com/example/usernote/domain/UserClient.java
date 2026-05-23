package com.example.usernote.domain;

import java.util.Optional;

public interface UserClient {

    Optional<UserSummary> findBySubject(String subject);

    record UserSummary(String subject, String username, String email) {}
}
