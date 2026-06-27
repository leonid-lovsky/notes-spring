package com.example.user.domain;

import java.util.Optional;

public interface UserFindByEmail {

    Optional<User> findByEmail(String email);
}
