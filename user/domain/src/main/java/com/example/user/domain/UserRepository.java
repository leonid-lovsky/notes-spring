package com.example.user.domain;

import java.util.*;

public interface UserRepository {

    boolean existsById(UUID id);

    Optional<User> findById(UUID id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    User add(User user);

    void replace(User user);

    void remove(UUID id);
}
