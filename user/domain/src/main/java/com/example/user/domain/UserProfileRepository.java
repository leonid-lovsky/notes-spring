package com.example.user.domain;

import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository {
    UserProfile save(UserProfile profile);
    Optional<UserProfile> findById(UUID id);
    Optional<UserProfile> findBySubject(String subject);
}
