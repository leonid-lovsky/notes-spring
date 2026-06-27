package com.example.user.domain;

import java.util.Optional;

public interface UserFindByUsernamePort {

    Optional<UserResponse> findByUsername(String username);
}
