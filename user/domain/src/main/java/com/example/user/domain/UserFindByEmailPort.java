package com.example.user.domain;

import java.util.Optional;

public interface UserFindByEmailPort {

    Optional<UserResponse> findByEmail(String email);
}
