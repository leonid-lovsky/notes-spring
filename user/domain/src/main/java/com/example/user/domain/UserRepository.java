package com.example.user.domain;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    boolean existsById(UUID id);

    Optional<User> findById(UUID id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    User save(User user);

    void deleteById(UUID id);
}
