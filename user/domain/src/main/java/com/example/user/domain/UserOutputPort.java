package com.example.user.domain;

import java.util.Optional;
import java.util.UUID;

public interface UserOutputPort {

    boolean existsById(UUID id);

    Optional<User> findById(UUID id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    void add(User user);

    void replace(User user);

    void remove(UUID id);
}
