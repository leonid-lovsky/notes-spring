package com.example.user.domain;

import java.util.Optional;
import java.util.UUID;

public interface UserFindByIdPort {

    Optional<UserResponse> findById(UUID id);
}
