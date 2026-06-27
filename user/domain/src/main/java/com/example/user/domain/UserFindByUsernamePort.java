package com.example.user.domain;

import java.util.Optional;

public interface UserFindByUsernamePort {

    Optional<User> findByUsername(String username);
}
