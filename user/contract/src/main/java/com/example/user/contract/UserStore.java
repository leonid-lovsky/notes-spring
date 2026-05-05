package com.example.user.contract;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStore {

    User create(User user);

    List<User> readAll();

    Optional<User> readById(UUID id);

    User update(User user);

    User replace(User user);

    void deleteById(UUID id);
}
