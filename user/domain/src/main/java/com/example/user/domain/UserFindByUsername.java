package com.example.user.domain;

import java.util.Optional;

public interface UserFindByUsername {

    Optional<User> findByUsername(String username);
}
