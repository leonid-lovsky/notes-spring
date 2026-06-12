package com.example.user.domain;

import java.util.List;
import java.util.UUID;

public interface UserUseCase {

    User create(String username, String email, String password);

    User findById(UUID id);

    User findByUsername(String username);

    User findByEmail(String email);

    List<User> findAll();

    User update(UUID id, String username, String email, String password);

    void delete(UUID id);
}
